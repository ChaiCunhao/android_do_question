package com.cch.do_question;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cch.do_question.util.GetSQLite;
import com.cch.do_question.util.Global;

public class BeforeQuestion extends AppCompatActivity {

    private TextView before_info;
    private Button continue_button;
    private Button reset_button;
    GetSQLite getSQLite;
    int all_number;//题目数量
    int correct_number;//正确数量
    int wrong_number;//错误数量
    int no_answer_number;//未答题数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_before_question);

        //初始化控件
        before_info=findViewById(R.id.before_info);
        continue_button=findViewById(R.id.start_question);
        reset_button=findViewById(R.id.reset_question);

        //显示答题情况信息
//        show_info();

        //继续答题按钮监听
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显式跳转
                Intent intent = new Intent(BeforeQuestion.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        //重新答题按钮监听
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清空数据库
                getSQLite.resetQuestionItem(BeforeQuestion.this);
                //显式跳转
                Intent intent = new Intent(BeforeQuestion.this, QuizActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {//表Activity已经变为可见状态了，并且出现在前台工作了，也就是指用户可见了
        super.onResume();
        show_info();
    }

    private void show_info(){
        //显示答题情况信息
        getSQLite=new GetSQLite(this);
        all_number= Global.question_size;
        correct_number=getSQLite.getCorrectNumber(this);
        wrong_number=getSQLite.getWrongNumber(this);
        no_answer_number=getSQLite.getNoAnswerNumber(this);
        before_info.setText("总题目数："+all_number+" 正确："+correct_number+" 错误："+wrong_number+" 未做："+no_answer_number);
    }

}