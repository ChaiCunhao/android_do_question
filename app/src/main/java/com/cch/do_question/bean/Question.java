package com.cch.do_question.bean;

import java.util.List;

public class Question {
    private int ID;//题目ID
    private String question_content;//问题内容
    private int type;//1代表判断题，2代表选择题
    private List<String> answers;//选项内容数组
    private int right;//正确选项
    private String image_url;//图片链接
    private String bestAnswer;//答案解析

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getBestAnswer() {
        return bestAnswer;
    }

    public void setBestAnswer(String bestAnswer) {
        this.bestAnswer = bestAnswer;
    }
}
