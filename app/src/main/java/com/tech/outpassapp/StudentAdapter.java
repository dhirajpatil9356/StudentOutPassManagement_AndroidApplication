package com.tech.outpassapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private Context context;
    private List<OutpassRequest> studentList;
    private boolean showButtons;
    private DatabaseReference outpassRequestsRef;
    private DatabaseReference acceptedRequestsRef;

    public StudentAdapter(Context context, List<OutpassRequest> studentList, boolean showButtons) {
        this.context = context;
        this.studentList = studentList;
        this.showButtons = showButtons;
        this.outpassRequestsRef = FirebaseDatabase.getInstance().getReference("outpassRequests");
        this.acceptedRequestsRef = FirebaseDatabase.getInstance().getReference("acceptedRequests");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OutpassRequest student = studentList.get(position);

        holder.studentName.setText(student.getStudentName());
        holder.rollNumber.setText(student.getRollNumber());
        holder.branch.setText(student.getBranch());
        holder.studentId.setText(student.getStudentID());
        holder.roomNumber.setText(student.getRoomNumber());

        if (showButtons) {
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.rejectButton.setVisibility(View.VISIBLE);

            holder.acceptButton.setOnClickListener(v -> acceptRequest(student, position));
            holder.rejectButton.setOnClickListener(v -> rejectRequest(student, position));
        } else {
            holder.acceptButton.setVisibility(View.GONE);
            holder.rejectButton.setVisibility(View.GONE);
        }
    }

    private void acceptRequest(OutpassRequest student, int position) {
        String requestId = student.getRollNumber();
        acceptedRequestsRef.child(requestId).setValue(student);

        outpassRequestsRef.child(requestId).removeValue();
        studentList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, studentList.size());
    }

    private void rejectRequest(OutpassRequest student, int position) {
        DatabaseReference outpassRequestsRef = FirebaseDatabase.getInstance().getReference("outpassRequests");
        String requestId = student.getRollNumber();

        outpassRequestsRef.child(requestId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    studentList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, studentList.size());
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseError", "Error deleting data: " + e.getMessage());
                });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, rollNumber, branch, studentId, roomNumber;
        Button acceptButton, rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.tvStudentName);
            rollNumber = itemView.findViewById(R.id.tvRollNumber);
            branch = itemView.findViewById(R.id.tvBranch);
            studentId = itemView.findViewById(R.id.tvStudentId);
            roomNumber = itemView.findViewById(R.id.tvRoomNumber);
            acceptButton = itemView.findViewById(R.id.btnAccept);
            rejectButton = itemView.findViewById(R.id.btnReject);
        }
    }
}
