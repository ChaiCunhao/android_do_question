package com.cch.do_question.util;

import com.cch.do_question.bean.Question;
import com.cch.do_question.bean.QuestionResponse;
import com.google.gson.Gson;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetService {

    //向服务器发送get请求，返回json字符串
    public static String getRequest(String url) {
        String result = null;
        String new_result = null;
        try {
            Response response = Request.get(url).execute();
            result = response.returnContent().asString();
            //解决返回数据中文乱码
            new_result = new String(result.getBytes("ISO-8859-1"), "utf-8");
//            System.out.println(new_result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new_result;
    }

    // 根据获取服务器的json字符串，返回题目对象
    public Question getQuestionFromJson(String s){
        // 创建Gson对象
        Gson gson = new Gson();
        // 将Json字符串转换为java对象
        QuestionResponse response = gson.fromJson(s, QuestionResponse.class);
        // 创建并返回一条题目数据
        Question q=new Question();
        q.setID(Integer.parseInt(response.getData().get("id")));
        q.setType(Integer.parseInt(response.getData().get("type")));
        q.setBestAnswer(response.getData().get("bestanswer"));
        q.setImage_url(response.getData().get("imageurl"));
        q.setQuestion_content(response.getData().get("question"));
        q.setRight(Integer.parseInt(response.getData().get("right")));
        if (Integer.parseInt(response.getData().get("type"))==2){//选择题
            List <String> answers = new ArrayList<>();
            answers.add(response.getData().get("a"));
            answers.add(response.getData().get("b"));
            answers.add(response.getData().get("c"));
            answers.add(response.getData().get("d"));
            q.setAnswers(answers);
        }else if(Integer.parseInt(response.getData().get("type"))==1) {//判断题
            List <String> answers2 = new ArrayList<>();
            answers2.add("正确");
            answers2.add("错误");
            q.setAnswers(answers2);
        }
        return q;
    }
}
