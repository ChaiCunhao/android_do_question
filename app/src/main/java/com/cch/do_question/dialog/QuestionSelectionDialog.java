package com.cch.do_question.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cch.do_question.R;
import com.cch.do_question.adapter.QuestionAdapter;
import com.cch.do_question.bean.QuestionItem;

import java.util.List;
import java.util.Objects;

//自定义的对话框类，用来展示题目选择面板
public class QuestionSelectionDialog extends DialogFragment {
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;

    private List<QuestionItem> questionItems;
    private int currentQuestionIndex;
    private QuestionDialogListener listener;

    public static QuestionSelectionDialog newInstance(List<QuestionItem> questionItems, int currentQuestionIndex) {
        QuestionSelectionDialog fragment = new QuestionSelectionDialog();
        fragment.questionItems = questionItems;
        fragment.currentQuestionIndex = currentQuestionIndex;
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //实现底部菜单
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//透明背景
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_question_selection, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 6);
        recyclerView.setLayoutManager(layoutManager);

        questionAdapter = new QuestionAdapter(questionItems, currentQuestionIndex);
        questionAdapter.setOnItemClickListener(new QuestionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int curIndex) {
                //实现接口中点击题号事件的逻辑
                listener.jumpToQuestion(curIndex);
                listener.onDismissDialog();
            }
        });
        recyclerView.setAdapter(questionAdapter);

        return view;
    }

    //定义一个接口,实现在 QuestionAdapter 中通知 Activity 关闭对话框和题目跳转的功能
    public interface QuestionDialogListener {
        void onDismissDialog();
        void jumpToQuestion(int questionIndex);
    }

    public void setQuestionDialogListener(QuestionDialogListener listener) {
        this.listener = listener;
    }

}

