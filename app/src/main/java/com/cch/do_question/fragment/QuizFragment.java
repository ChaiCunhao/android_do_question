package com.cch.do_question.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cch.do_question.QuizActivity;
import com.cch.do_question.R;
import com.cch.do_question.bean.Question;
import com.cch.do_question.bean.QuestionItem;
import com.cch.do_question.util.GetSQLite;
import com.cch.do_question.util.GetService;
import com.cch.do_question.util.Global;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    private Question question; //题目对象
    private Handler mHandler; //消息处理器
    private QuestionItem questionItem; //题目状态信息
    private URL img_url;//图片链接

    private TextView questionTypeText;//题目类型
    private TextView questionTextView;//题目内容
    private ImageView questionImage;//题目配图
    private RadioGroup optionsRadioGroup;//选项组
    private Button submitButton;//提交按钮
    private Button preButton;//上一题按钮
    private Button nextButton;//下一题按钮
    private TextView bestAnswer;//答案解析

    private int currentQuestionIndex;//当前题目下标
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

        //关联控件
        questionTypeText = view.findViewById(R.id.questionType);
        questionTextView = view.findViewById(R.id.questionTextView);
        questionImage = view.findViewById(R.id.questionImage);
        optionsRadioGroup = view.findViewById(R.id.optionsRadioGroup);
        submitButton = view.findViewById(R.id.submitButton);
        preButton = view.findViewById(R.id.prebutton);
        nextButton = view.findViewById(R.id.nextbutton);
        bestAnswer = view.findViewById(R.id.bestAnswer);

        getQuestion();//请求当前题目数据

        mHandler=new Handler(new Handler.Callback() {//接收消息，做出处理
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == 123) {
                    //接收到json字符串,转化为question对象
                    String json=(String) msg.obj;
                    GetService getService = new GetService();
                    question = getService.getQuestionFromJson(json);
                    displayQuestion();
                }else if(msg.what == 456){
                    //接收到图片（bitmap），将其加载出来
                    Bitmap bitmap= (Bitmap) msg.obj;
                    questionImage.setImageBitmap(bitmap);
                    questionImage.setVisibility(View.VISIBLE);//图片显示
                }
                return true;
            }
        });

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
        //新建子线程请求题目配图
        questionImage.setVisibility(View.GONE);//不显示，不占布局空间
        if (!question.getImage_url().isEmpty()){
            try {
                img_url = new URL(question.getImage_url());
                requestImg(img_url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        // 显示当前题目类型
        if (question.getType() == 1){//判断题
            questionTypeText.setText("判断题");
        }else{//选择题
            questionTypeText.setText("选择题");
        }
        // 显示当前题目问题
        questionTextView.setText(currentQuestionIndex+1+". "+question.getQuestion_content());
        // 清空选项组
        optionsRadioGroup.clearCheck();
        optionsRadioGroup.removeAllViews();
        // 添加选项
        for (int i = 0; i < question.getAnswers().size(); i++) {
            RadioButton radioButton = new RadioButton(requireContext());
            radioButton.setText(question.getAnswers().get(i));
            radioButton.setId(i);
            if (questionItem.isAnswered()){// 若已经答过该题
                radioButton.setClickable(false);// 按钮不可点击（or radioButton.setEnabled(false);）
                if (questionItem.getSelected_Index() == i){// 若之前选中的是该按钮
                    radioButton.setChecked(true);// 默认选中
                    radioButton.setTextColor(Color.rgb(255,0,0));// 假设选择为错误，置红
                }
                if (question.getRight()-1 == i){
                    radioButton.setTextColor(Color.rgb(0, 255, 0));//正确选项置绿
                }
            }
            optionsRadioGroup.addView(radioButton);
        }
        // 显示题目解析
        if (questionItem.isAnswered()){
            bestAnswer.setText(question.getBestAnswer());
        }else{
            bestAnswer.setText("");
        }

    }

    public void displayQuestion(int cIndex,QuestionItem qItem){
        currentQuestionIndex = cIndex;
        getQuestion();
        questionItem=qItem;
    }

    private void checkAnswer() {
        if (questionItem.isAnswered()){//若已提交过该题
            Toast.makeText(requireContext(), "本题已经提交过啦！", Toast.LENGTH_SHORT).show();
        }else{//若没提交过该题
            int selectedRadioButtonId = optionsRadioGroup.getCheckedRadioButtonId();
            if (selectedRadioButtonId == -1) {// 若没有选中答案
                Toast.makeText(requireContext(), "请选择一个答案", Toast.LENGTH_SHORT).show();
            } else {
                // RadioButton selectedRadioButton = requireView().findViewById(selectedRadioButtonId);
                // String selectedAnswer = selectedRadioButton.getText().toString();
                // System.out.println(selectedAnswer);
                questionItem.setSelected_Index(selectedRadioButtonId);
                questionItem.setAnswered(true);
                if (selectedRadioButtonId == question.getRight() - 1) {
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

    private void getQuestion(){//从网络获取当前题目数据(线程)
        new Thread(){
            public void run(){
                int q_id = currentQuestionIndex+1;
                String json = GetService.getRequest(Global.questi0nURl+q_id);
                Message message=Message.obtain();
                message.what=123;
                message.obj=json;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    private void requestImg(final URL imgUrl) {//请求网络图片，请求结果为bitmap
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(imgUrl.openStream());
                    //发送消息进行UI更新
                    Message message=new Message();
                    message.what=456;
                    message.obj=bitmap;
                    mHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}