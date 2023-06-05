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
    private List<QuestionItem> questionItems;
    private int currentQuestionIndex;
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

        GetSQLite getSQLite = new GetSQLite();
        questionItems=getSQLite.setQuestionItemList();
        correctCount=0;
        wrongCount=0;
        totalQuestions=questionItems.size();

        updateInfoBar();

        // 添加 QuizFragment 到 Activity 中
        quizFragment = new QuizFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, quizFragment).commit();

        // 点击底部信息栏显示题目选择面板
        infoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuestionSelectionDialog();
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

    private void showQuestionSelectionDialog() {
        dialog = QuestionSelectionDialog.newInstance(questionItems, currentQuestionIndex);
        dialog.setQuestionDialogListener(this);
        dialog.show(getSupportFragmentManager(), "QuestionSelectionDialog");
    }

    public void jumpToQuestion(int questionIndex) {
        // 实现接口中跳转到指定的题目的逻辑
        if (questionIndex >= 0 && questionIndex < questionItems.size()) {
            currentQuestionIndex = questionIndex;
            // 更新题目的显示等逻辑
            quizFragment.displayQuestion(currentQuestionIndex,questionItems.get(currentQuestionIndex));
            updateInfoBar();
        }
    }

    @Override
    public void onDismissDialog() {
        // 实现接口中关闭对话框的逻辑
        dialog.dismiss();
    }

    @Override
    public void doquestion(QuestionItem questionItem) {//实现接口中更新答题情况的逻辑
        questionItems.get(questionItem.getQuestionNumber()-1).setAnswered(true);
        questionItems.get(questionItem.getQuestionNumber()-1).setCorrect(questionItem.isCorrect());
        questionItems.get(questionItem.getQuestionNumber()-1).setSelected_Index(questionItem.getSelected_Index());
        if (questionItem.isCorrect()){
            correctCount++;
            if (currentQuestionIndex < questionItems.size()-1) {currentQuestionIndex++;}// 切换到下一题
        } else{
            wrongCount++;
        }
        quizFragment.displayQuestion(currentQuestionIndex,questionItems.get(currentQuestionIndex));
        updateInfoBar();
    }

}
