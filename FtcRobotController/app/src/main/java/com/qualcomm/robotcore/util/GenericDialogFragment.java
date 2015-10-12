package com.qualcomm.robotcore.util;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class GenericDialogFragment extends DialogFragment {
   public Dialog onCreateDialog(Bundle var1) {
      String var2 = this.getArguments().getString("dialogMsg", "App error condition!");
      Builder var3 = new Builder(this.getActivity());
      var3.setMessage(var2).setPositiveButton("OK", new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
         }
      });
      return var3.create();
   }
}
