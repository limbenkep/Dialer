package se.miun.holi1900.dt031g.dialer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class AboutDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.about_dialog_text)
                .setTitle(R.string.about_dialog_title)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    // Close dialog box
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
