package com.cch.do_question;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity {
    private TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        infoTextView = findViewById(R.id.infoTextView);

        // 添加 QuizFragment 到 Activity 中
        QuizFragment quizFragment = new QuizFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, quizFragment).commit();
    }

    public void updateQuizInfo(int correctAnswers, int wrongAnswers, int totalQuestions, int currentQuestionIndex) {
        String infoText = "答对题目数：" + correctAnswers + "  答错题目数：" + wrongAnswers + "  总题目数：" + totalQuestions + "  当前题目Id：" + (currentQuestionIndex + 1);
        infoTextView.setText(infoText);
    }
}
