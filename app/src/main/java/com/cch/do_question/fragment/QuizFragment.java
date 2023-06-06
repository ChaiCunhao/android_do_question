package com.cch.do_question.fragment;

import android.content.Context;
import android.graphics.Color;
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
import com.cch.do_question.bean.QuestionItem;
import com.cch.do_question.util.GetSQLite;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    // 定义题目数组
    private List<Question> questions = new ArrayList<Question>();
    private QuestionItem questionItem;

    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private Button submitButton;
    private Button preButton;
    private Button nextButton;

    private int currentQuestionIndex = 0;//当前题目下标
    private int correctAnswers = 0;//正确回答数目
    private int wrongAnswers = 0;//错误回答数目
    private  DoQuestionListener listener;

    //创建一个接口
    public interface DoQuestionListener{
        void doquestion(QuestionItem questionItem);
        void pre_question();
        void next_question();
    }

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        currentQuestionIndex = ((QuizActivity) context).getCurrentQuestionIndex();
        questionItem = ((QuizActivity) context).getCurrentQuestionItem();
        try {
            listener = (DoQuestionListener) context;
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
        preButton = view.findViewById(R.id.prebutton);
        nextButton = view.findViewById(R.id.nextbutton);

        GetSQLite getSQLite = new GetSQLite(getActivity());
        questions= getSQLite.setQuestionList();

        displayQuestion();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });
        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.pre_question();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.next_question();
            }
        });

        return view;
    }

    private void displayQuestion() {
        // 显示当前题目
        questionTextView.setText(questions.get(currentQuestionIndex).getQuestion_content());
        // 清空选项
        optionsRadioGroup.clearCheck();
        optionsRadioGroup.removeAllViews();
        // 添加选项
        for (int i = 0; i < questions.get(currentQuestionIndex).getAnswers().size(); i++) {
            RadioButton radioButton = new RadioButton(requireContext());
            radioButton.setText(questions.get(currentQuestionIndex).getAnswers().get(i));
            radioButton.setId(i);
            if (questionItem.isAnswered()){// 若已经答过该题
                radioButton.setClickable(false);// 按钮不可点击（or radioButton.setEnabled(false);）
                if (questionItem.getSelected_Index() == i){// 若之前选中的是该按钮
                    radioButton.setChecked(true);// 默认选中
                    radioButton.setTextColor(Color.rgb(255,0,0));// 假设选择为错误，置红
                }
                if (questions.get(currentQuestionIndex).getRight()-1 == i){
                    radioButton.setTextColor(Color.rgb(0, 255, 0));//正确选项置绿
                }
            }
            optionsRadioGroup.addView(radioButton);
        }

    }

    public void displayQuestion(int cIndex,QuestionItem qItem){
        currentQuestionIndex = cIndex;
        questionItem=qItem;
        displayQuestion();
    }

    private void checkAnswer() {
        if (questionItem.isAnswered()){//若已提交过该题
            Toast.makeText(requireContext(), "本题已经提交过啦！", Toast.LENGTH_SHORT).show();
        }else{//若没提交过该题
            int selectedRadioButtonId = optionsRadioGroup.getCheckedRadioButtonId();
            if (selectedRadioButtonId == -1) {
                // 没有选中答案
                Toast.makeText(requireContext(), "请选择一个答案", Toast.LENGTH_SHORT).show();
            } else {
                // RadioButton selectedRadioButton = requireView().findViewById(selectedRadioButtonId);
                // String selectedAnswer = selectedRadioButton.getText().toString();
                // System.out.println(selectedAnswer);
                questionItem.setSelected_Index(selectedRadioButtonId);
                questionItem.setAnswered(true);
                if (selectedRadioButtonId == questions.get(currentQuestionIndex).getRight() - 1) {
                    questionItem.setCorrect(true);
                    Toast.makeText(requireContext(), "回答正确！", Toast.LENGTH_SHORT).show();
                    listener.doquestion(questionItem);
                } else {
                    questionItem.setCorrect(false);
                    Toast.makeText(requireContext(), "回答错误！", Toast.LENGTH_SHORT).show();
                    listener.doquestion(questionItem);
                }
            }
        }

    }

}