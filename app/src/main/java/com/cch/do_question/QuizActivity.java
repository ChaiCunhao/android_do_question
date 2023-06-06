package com.cch.do_question;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.cch.do_question.bean.QuestionItem;
import com.cch.do_question.dialog.QuestionSelectionDialog;
import com.cch.do_question.fragment.QuizFragment;
import com.cch.do_question.util.GetSQLite;

import java.util.List;
import java.util.Locale;
import android.content.Context;

// QuizActivity 实现QuestionDialogListener接口，并在实现方法中实现关闭对话框的逻辑
public class QuizActivity extends AppCompatActivity implements QuestionSelectionDialog.QuestionDialogListener, QuizFragment.DoQuestionListener {
    private TextView infoTextView;
    GetSQLite getSQLite;
    private List<QuestionItem> questionItems;
    private int currentQuestionIndex = 0;
    QuizFragment quizFragment;
    QuestionSelectionDialog dialog;
    private int correctCount;
    private int wrongCount;
    private int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_quiz);

        // 初始化底部信息栏
        infoTextView = findViewById(R.id.infoTextView);

        getSQLite = new GetSQLite(this);
        questionItems=getSQLite.getQuestionItems(this);
        correctCount=0;
        wrongCount=0;
        totalQuestions=questionItems.size();

        updateInfoBar();

        // 添加 QuizFragment 到 Activity 中
        quizFragment = new QuizFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, quizFragment).commit();

        //创建题目选择面板
        dialog = QuestionSelectionDialog.newInstance(questionItems, currentQuestionIndex);
        dialog.setQuestionDialogListener(this);

        // 点击底部信息栏显示题目选择面板
        infoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示题目选择面板
                dialog.show(getSupportFragmentManager(), "QuestionSelectionDialog");
            }
        });

    }

    public int getCurrentQuestionIndex(){
        return currentQuestionIndex;
    }
    public QuestionItem getCurrentQuestionItem(){return questionItems.get(currentQuestionIndex);}

    private void updateInfoBar() {
        String info = String.format(Locale.getDefault(), "Correct: %d, Wrong: %d, Total: %d, CurrentID: %d",
            correctCount, wrongCount, totalQuestions, currentQuestionIndex+1);
        infoTextView.setText(info);
    }

    public void jumpToQuestion(int questionIndex) {
        // 实现接口中跳转到指定的题目的逻辑
        if (questionIndex >= 0 && questionIndex < questionItems.size()) {
            currentQuestionIndex = questionIndex;
            // 更新题目的显示等逻辑
            quizFragment.displayQuestion(currentQuestionIndex,questionItems.get(currentQuestionIndex));
            //更新底部信息栏
            updateInfoBar();
            //更新选题面板当前项
            dialog.updateItem(currentQuestionIndex);
        }
    }

    @Override
    public void onDismissDialog() {
        // 实现接口中关闭对话框的逻辑
        dialog.dismiss();
    }

    @Override
    public void doquestion(QuestionItem questionItem) {//实现接口中更新答题情况的逻辑
        //更新sqlite数据库
        getSQLite.updateQuestionItem(this,questionItem);
        //更新选题面板该项数据
        dialog.updateItem(questionItem);
        //更新内存数据
        questionItems.get(questionItem.getQuestionNumber()-1).setAnswered(true);
        questionItems.get(questionItem.getQuestionNumber()-1).setCorrect(questionItem.isCorrect());
        questionItems.get(questionItem.getQuestionNumber()-1).setSelected_Index(questionItem.getSelected_Index());
        if (questionItem.isCorrect()){
            correctCount++;
            if (currentQuestionIndex < questionItems.size()-1) {currentQuestionIndex++;}// 切换到下一题
        } else{
            wrongCount++;
        }
        quizFragment.displayQuestion(currentQuestionIndex,questionItems.get(currentQuestionIndex));//更新题目展示
        updateInfoBar();//更新底部信息栏
        dialog.updateItem(currentQuestionIndex);//更新选题面板当前项

    }

    @Override
    public void pre_question(){//实现接口中切换上一题的逻辑
        if (currentQuestionIndex > 0) {currentQuestionIndex--;}// 切换到上一题
        quizFragment.displayQuestion(currentQuestionIndex,questionItems.get(currentQuestionIndex));//题目展示
        updateInfoBar();//更新信息栏
        dialog.updateItem(currentQuestionIndex);//更新选题面板当前项
    }

    @Override
    public void next_question(){//实现接口中切换下一题的逻辑
        if (currentQuestionIndex < questionItems.size()-1) {currentQuestionIndex++;}// 切换到下一题
        quizFragment.displayQuestion(currentQuestionIndex,questionItems.get(currentQuestionIndex));//题目展示
        updateInfoBar();//更新信息栏
        dialog.updateItem(currentQuestionIndex);//更新选题面板当前项
    }

}
