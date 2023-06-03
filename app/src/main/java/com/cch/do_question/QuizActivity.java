package com.cch.do_question;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
            quizFragment.updateQuestion(currentQuestionIndex);
            updateInfoBar();
        }
    }

    @Override
    public void onDismissDialog() {
        // 实现接口中关闭对话框的逻辑
        dialog.dismiss();
    }

    @Override
    public void doquestion(int question_index, boolean isright) {//实现接口中更新答题情况的逻辑
        questionItems.get(question_index).setAnswered(true);
        questionItems.get(question_index).setCorrect(isright);
        if (isright){
            correctCount++;
        } else{
            wrongCount++;
        }
        if (currentQuestionIndex < questionItems.size()-1) {
            // 切换到下一题
            currentQuestionIndex++;
            quizFragment.updateQuestion(currentQuestionIndex);
        }
        updateInfoBar();
    }

}
