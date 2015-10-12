package com.qualcomm.ftcdriverstation;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OpModeSelectionDialogFragment extends DialogFragment {
   private OpModeSelectionDialogFragment.OpModeSelectionDialogListener listener = null;
   private String[] opModes = new String[0];

   public Dialog onCreateDialog(Bundle var1) {
      View var2 = LayoutInflater.from(this.getActivity()).inflate(2130903054, (ViewGroup)null);
      Builder var3 = new Builder(this.getActivity());
      var3.setCustomTitle(var2);
      var3.setTitle(2131361817);
      var3.setItems(this.opModes, new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            if(OpModeSelectionDialogFragment.this.listener != null) {
               OpModeSelectionDialogFragment.this.listener.onSelectionClick(OpModeSelectionDialogFragment.this.opModes[var2]);
            }

         }
      });
      return var3.create();
   }

   public void onStart() {
      super.onStart();
      Dialog var1 = this.getDialog();
      var1.findViewById(var1.getContext().getResources().getIdentifier("android:id/titleDivider", (String)null, (String)null)).setBackgroundColor(this.getResources().getColor(2131296266));
   }

   public void setOnSelectionDialogListener(OpModeSelectionDialogFragment.OpModeSelectionDialogListener var1) {
      this.listener = var1;
   }

   public void setOpModes(String[] var1) {
      this.opModes = var1;
   }

   public interface OpModeSelectionDialogListener {
      void onSelectionClick(String var1);
   }
}
