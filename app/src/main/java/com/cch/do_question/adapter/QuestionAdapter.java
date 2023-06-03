package com.cch.do_question.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cch.do_question.QuizActivity;
import com.cch.do_question.R;
import com.cch.do_question.bean.QuestionItem;
import com.cch.do_question.dialog.QuestionSelectionDialog;

import java.util.List;

//创建一个自定义的QuestionAdapter，用于显示题目列表
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private List<QuestionItem> questionItems;
    private int currentQuestionIndex;
    private  OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener{
        void onItemClick(int curIndex);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public QuestionAdapter(List<QuestionItem> questionItems, int currentQuestionIndex) {
        this.questionItems = questionItems;
        this.currentQuestionIndex = currentQuestionIndex;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        QuestionItem questionItem = questionItems.get(position);
        holder.questionNumberTextView.setText(String.valueOf(questionItem.getQuestionNumber()));

        // 设置题号的背景颜色
        if (questionItem.isAnswered()) {
            if (questionItem.isCorrect()) {
//                holder.questionNumberTextView.setBackgroundResource(R.drawable.bg_question_number_correct);
                holder.linearLayout.setBackgroundResource(R.drawable.bg_question_number_correct);
            } else {
//                holder.questionNumberTextView.setBackgroundResource(R.drawable.bg_question_number_wrong);
                holder.linearLayout.setBackgroundResource(R.drawable.bg_question_number_wrong);
            }
        } else {
//            holder.questionNumberTextView.setBackgroundResource(R.drawable.bg_question_number_default);
            holder.linearLayout.setBackgroundResource(R.drawable.bg_question_number_default);
        }

        // 处理题号的点击事件
        if (onItemClickListener != null){
            holder.questionNumberTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 跳转到点击的题目
                    int selectedQuestionIndex = questionItem.getQuestionNumber() - 1;
                    if (selectedQuestionIndex >= 0 && selectedQuestionIndex < questionItems.size()) {
                        onItemClickListener.onItemClick(selectedQuestionIndex);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return questionItems.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionNumberTextView;
        LinearLayout linearLayout;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumberTextView = itemView.findViewById(R.id.questionNumberTextView);
            linearLayout=itemView.findViewById(R.id.questionLinear);
        }
    }

}
