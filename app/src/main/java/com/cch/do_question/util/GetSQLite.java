package com.cch.do_question.util;

import com.cch.do_question.bean.Question;
import com.cch.do_question.bean.QuestionItem;

import java.util.ArrayList;
import java.util.List;

public class GetSQLite {
    // 定义题目数组
    private String[] question_contents = {
            "问题1", "问题2", "问题3", "问题4", "问题5", "问题6", "问题7", "问题8", "问题9", "问题10",
            "问题11", "问题12", "问题13", "问题14", "问题15", "问题16", "问题17", "问题18", "问题19", "问题20",
            "问题21", "问题22", "问题23", "问题24", "问题25", "问题26", "问题27", "问题28", "问题29", "问题30",
            "问题31", "问题32", "问题33", "问题34", "问题35", "问题36", "问题37", "问题38", "问题39", "问题40",
            "问题41", "问题42", "问题43", "问题44", "问题45", "问题46", "问题47", "问题48", "问题49", "问题50",
            "问题51", "问题52", "问题53", "问题54", "问题55", "问题56", "问题57", "问题58", "问题59", "问题60",
            "问题61", "问题62", "问题63", "问题64", "问题65", "问题66", "问题67", "问题68", "问题69", "问题70",
            "问题71", "问题72", "问题73", "问题74", "问题75", "问题76", "问题77", "问题78", "问题79", "问题80",
            "问题81", "问题82", "问题83", "问题84", "问题85", "问题86", "问题87", "问题88", "问题89", "问题90",
            "问题91", "问题92", "问题93", "问题94", "问题95", "问题96", "问题97", "问题98", "问题99", "问题100",
    };
    // 定义答案数组
    private String[][] answers = {
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
    };
    //答案
    private int[] rights = {
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
    };

    /* 把问题信息封装成集合，为后面调用数据库数据替换做准备*/
    public List<Question> setQuestionList() {
        List<Question> list = new ArrayList<>();
        for (int i = 0; i < question_contents.length; i++) {
            Question question=new Question();
            question.setID(i+1);
            question.setQuestion_content(question_contents[i]);
            List<String> answer_list = new ArrayList<>();
            for (int j = 0; j < answers[i].length; j++) {
                answer_list.add(answers[i][j]);
            }
            question.setAnswers(answer_list);
            question.setRight(rights[i]);
            list.add(question);
        }
        return list;
    }

    /* 把问题信息封装成集合，为后面调用数据库数据替换做准备*/
    public List<QuestionItem> setQuestionItemList() {
        List<QuestionItem> list2 = new ArrayList<>();
        for (int i = 0; i < question_contents.length; i++) {
            QuestionItem questionItem=new QuestionItem(i+1,false,false);
            list2.add(questionItem);
        }
        return list2;
    }

}