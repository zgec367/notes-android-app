package com.example.notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.hsalf.smilerating.SmileRating;

public class FeelingDialog extends AppCompatDialogFragment {
    private String rating = "";
    private FeelingDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.feeling_dialog, null);
        SmileRating smileRating = view.findViewById(R.id.smile_rating);
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {
                switch (smiley) {
                    case SmileRating.BAD:
                        rating = "Bad";
                        break;
                    case SmileRating.GOOD:
                        rating = "Good";
                        break;
                    case SmileRating.GREAT:
                        rating = "Great";
                        break;
                    case SmileRating.OKAY:
                        rating = "Okay";
                        break;
                    case SmileRating.TERRIBLE:
                        rating = "Terrible";
                        break;
                }
            }
        });
        builder.setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyFeeling(rating);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (FeelingDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement");
        }
    }
    public interface FeelingDialogListener {
        void applyFeeling(String rating);
    }

}
