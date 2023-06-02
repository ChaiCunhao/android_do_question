package com.cch.do_question;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizFragment extends Fragment {

    // 定义题目数组
    private String[] questions = {
            "问题1",
            "问题2",
            "问题3"
    };

    // 定义答案数组
    private String[][] answers = {
            {"a", "b", "c"},
            {"x", "y"},
            {"111", "2222", "3333333", "44"}
    };

    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private Button submitButton;

    private int currentQuestionIndex = 0;//当前题目下标
    private int correctAnswers = 0;//正确回答数目
    private int wrongAnswers = 0;//错误回答数目

    private int[] right = {2, 1, 4};//答案

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        questionTextView = view.findViewById(R.id.questionTextView);
        optionsRadioGroup = view.findViewById(R.id.optionsRadioGroup);
        submitButton = view.findViewById(R.id.submitButton);

        displayQuestion();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });

        return view;
    }

    private void displayQuestion() {
        // 显示当前题目
        questionTextView.setText(questions[currentQuestionIndex]);
        // 清空选项
        optionsRadioGroup.removeAllViews();

        for (int i = 0; i < answers[currentQuestionIndex].length; i++) {
            RadioButton radioButton = new RadioButton(requireContext());
            radioButton.setText(answers[currentQuestionIndex][i]);
            radioButton.setId(i);
            optionsRadioGroup.addView(radioButton);
        }
    }

    private void checkAnswer() {
        int selectedRadioButtonId = optionsRadioGroup.getCheckedRadioButtonId();

        if (selectedRadioButtonId == -1) {
            // 没有选中答案
            Toast.makeText(requireContext(), "请选择一个答案", Toast.LENGTH_SHORT).show();
        } else {
            // RadioButton selectedRadioButton = requireView().findViewById(selectedRadioButtonId);
            // String selectedAnswer = selectedRadioButton.getText().toString();
            // System.out.println(selectedAnswer);
            if (selectedRadioButtonId == right[currentQuestionIndex] - 1) {
                Toast.makeText(requireContext(), "回答正确！", Toast.LENGTH_SHORT).show();
                correctAnswers++;
            } else {
                Toast.makeText(requireContext(), "回答错误！", Toast.LENGTH_SHORT).show();
                wrongAnswers++;
            }

            // 切换到下一题
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.length) {
                displayQuestion();
            } else {
                Toast.makeText(requireContext(), "已经回答完所有题目", Toast.LENGTH_SHORT).show();
                // 做完题目后可以执行相应的操作，如显示得分等
            }

            // 更新底部横栏信息
            if (getActivity() instanceof QuizActivity) {
                ((QuizActivity) getActivity()).updateQuizInfo(correctAnswers, wrongAnswers, questions.length, currentQuestionIndex);
            }
        }
    }

}