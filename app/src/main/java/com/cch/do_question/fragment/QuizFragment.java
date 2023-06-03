package com.cch.do_question.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cch.do_question.QuizActivity;
import com.cch.do_question.R;
import com.cch.do_question.bean.Question;
import com.cch.do_question.util.GetSQLite;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    // 定义题目数组
    private List<Question> questions = new ArrayList<Question>();

    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private Button submitButton;

    private int currentQuestionIndex = 0;//当前题目下标
    private int correctAnswers = 0;//正确回答数目
    private int wrongAnswers = 0;//错误回答数目
    private  DoQuestionListener listener;

    //创建一个接口
    public interface DoQuestionListener{
        void doquestion(int question_index, boolean isright);
    }

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        currentQuestionIndex = ((QuizActivity) context).getCurrentQuestionIndex();
        try {
            listener= (DoQuestionListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        questionTextView = view.findViewById(R.id.questionTextView);
        optionsRadioGroup = view.findViewById(R.id.optionsRadioGroup);
        submitButton = view.findViewById(R.id.submitButton);

        GetSQLite getSQLite = new GetSQLite();
        questions= getSQLite.setQuestionList();

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
        questionTextView.setText(questions.get(currentQuestionIndex).getQuestion_content());
        // 清空选项
        optionsRadioGroup.removeAllViews();

        for (int i = 0; i < questions.get(currentQuestionIndex).getAnswers().size(); i++) {
            RadioButton radioButton = new RadioButton(requireContext());
            radioButton.setText(questions.get(currentQuestionIndex).getAnswers().get(i));
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
            if (selectedRadioButtonId == questions.get(currentQuestionIndex).getRight() - 1) {
                Toast.makeText(requireContext(), "回答正确！", Toast.LENGTH_SHORT).show();
                listener.doquestion(currentQuestionIndex,true);
            } else {
                Toast.makeText(requireContext(), "回答错误！", Toast.LENGTH_SHORT).show();
                listener.doquestion(currentQuestionIndex,false);
            }
        }
    }

    public void updateQuestion(int cIndex){
        currentQuestionIndex = cIndex;
        displayQuestion();
    }

}