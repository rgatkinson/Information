package com.qualcomm.robotcore.util;

import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.content.Context;
import android.app.AlertDialog$Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;

public class GenericDialogFragment extends DialogFragment
{
    public Dialog onCreateDialog(final Bundle bundle) {
        final String string = this.getArguments().getString("dialogMsg", "App error condition!");
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
        alertDialog$Builder.setMessage((CharSequence)string).setPositiveButton((CharSequence)"OK", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
            }
        });
        return (Dialog)alertDialog$Builder.create();
    }
}
