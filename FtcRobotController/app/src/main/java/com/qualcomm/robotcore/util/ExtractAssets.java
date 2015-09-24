package com.qualcomm.robotcore.util;

import java.util.Iterator;
import android.util.Log;
import java.io.IOException;
import android.os.Environment;
import java.util.ArrayList;
import android.content.Context;

public class ExtractAssets
{
    private static final String a;
    
    static {
        a = ExtractAssets.class.getSimpleName();
    }
    
    public static ArrayList<String> ExtractToStorage(final Context context, final ArrayList<String> list, final boolean b) throws IOException {
        if (!b && !"mounted".equals(Environment.getExternalStorageState())) {
            throw new IOException("External Storage not accessible");
        }
        final ArrayList<String> list2 = new ArrayList<String>();
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            a(context, iterator.next(), b, list2);
            if (list2 != null) {
                Log.d(ExtractAssets.a, "got " + list2.size() + " elements");
            }
        }
        return list2;
    }
    
    private static ArrayList<String> a(final Context p0, final String p1, final boolean p2, final ArrayList<String> p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore          4
        //     3: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //     6: new             Ljava/lang/StringBuilder;
        //     9: dup            
        //    10: invokespecial   java/lang/StringBuilder.<init>:()V
        //    13: ldc             "Extracting assests for "
        //    15: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    18: aload_1        
        //    19: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    22: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    25: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    28: pop            
        //    29: aload_0        
        //    30: invokevirtual   android/content/Context.getAssets:()Landroid/content/res/AssetManager;
        //    33: astore          6
        //    35: aload           6
        //    37: aload_1        
        //    38: invokevirtual   android/content/res/AssetManager.list:(Ljava/lang/String;)[Ljava/lang/String;
        //    41: astore          52
        //    43: aload           52
        //    45: astore          8
        //    47: aload           8
        //    49: arraylength    
        //    50: ifne            653
        //    53: aload           6
        //    55: aload_1        
        //    56: invokevirtual   android/content/res/AssetManager.open:(Ljava/lang/String;)Ljava/io/InputStream;
        //    59: astore          26
        //    61: aload           26
        //    63: astore          15
        //    65: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //    68: new             Ljava/lang/StringBuilder;
        //    71: dup            
        //    72: invokespecial   java/lang/StringBuilder.<init>:()V
        //    75: ldc             "File: "
        //    77: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    80: aload_1        
        //    81: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    84: ldc             " opened for streaming"
        //    86: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    89: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    92: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    95: pop            
        //    96: aload_1        
        //    97: getstatic       java/io/File.separator:Ljava/lang/String;
        //   100: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   103: ifne            127
        //   106: new             Ljava/lang/StringBuilder;
        //   109: dup            
        //   110: invokespecial   java/lang/StringBuilder.<init>:()V
        //   113: getstatic       java/io/File.separator:Ljava/lang/String;
        //   116: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   119: aload_1        
        //   120: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   123: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   126: astore_1       
        //   127: iload_2        
        //   128: ifeq            221
        //   131: aload_0        
        //   132: invokevirtual   android/content/Context.getFilesDir:()Ljava/io/File;
        //   135: astore          29
        //   137: aload           29
        //   139: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //   142: aload_1        
        //   143: invokevirtual   java/lang/String.concat:(Ljava/lang/String;)Ljava/lang/String;
        //   146: astore          30
        //   148: aload_3        
        //   149: ifnull          272
        //   152: aload_3        
        //   153: aload           30
        //   155: invokevirtual   java/util/ArrayList.contains:(Ljava/lang/Object;)Z
        //   158: ifeq            272
        //   161: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //   164: new             Ljava/lang/StringBuilder;
        //   167: dup            
        //   168: invokespecial   java/lang/StringBuilder.<init>:()V
        //   171: ldc             "Ignoring Duplicate entry for "
        //   173: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   176: aload           30
        //   178: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   181: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   184: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   187: pop            
        //   188: aload           15
        //   190: ifnull          206
        //   193: aload           15
        //   195: invokevirtual   java/io/InputStream.close:()V
        //   198: iconst_0       
        //   199: ifeq            206
        //   202: aconst_null    
        //   203: invokevirtual   java/io/FileOutputStream.close:()V
        //   206: aload_3        
        //   207: areturn        
        //   208: astore          7
        //   210: aload           7
        //   212: invokevirtual   java/io/IOException.printStackTrace:()V
        //   215: aconst_null    
        //   216: astore          8
        //   218: goto            47
        //   221: aload_0        
        //   222: aconst_null    
        //   223: invokevirtual   android/content/Context.getExternalFilesDir:(Ljava/lang/String;)Ljava/io/File;
        //   226: astore          51
        //   228: aload           51
        //   230: astore          29
        //   232: goto            137
        //   235: astore          47
        //   237: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //   240: ldc             "Unable to close in stream"
        //   242: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   245: pop            
        //   246: aload           47
        //   248: invokevirtual   java/io/IOException.printStackTrace:()V
        //   251: goto            198
        //   254: astore          49
        //   256: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //   259: ldc             "Unable to close out stream"
        //   261: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   264: pop            
        //   265: aload           49
        //   267: invokevirtual   java/io/IOException.printStackTrace:()V
        //   270: aload_3        
        //   271: areturn        
        //   272: aload           30
        //   274: getstatic       java/io/File.separatorChar:C
        //   277: invokevirtual   java/lang/String.lastIndexOf:(I)I
        //   280: istore          31
        //   282: aload           30
        //   284: iconst_0       
        //   285: iload           31
        //   287: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   290: astore          32
        //   292: aload           30
        //   294: iload           31
        //   296: aload           30
        //   298: invokevirtual   java/lang/String.length:()I
        //   301: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   304: astore          33
        //   306: new             Ljava/io/File;
        //   309: dup            
        //   310: aload           32
        //   312: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   315: astore          34
        //   317: aload           34
        //   319: invokevirtual   java/io/File.mkdirs:()Z
        //   322: ifeq            352
        //   325: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //   328: new             Ljava/lang/StringBuilder;
        //   331: dup            
        //   332: invokespecial   java/lang/StringBuilder.<init>:()V
        //   335: ldc             "Dir created "
        //   337: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   340: aload           32
        //   342: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   345: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   348: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   351: pop            
        //   352: new             Ljava/io/FileOutputStream;
        //   355: dup            
        //   356: new             Ljava/io/File;
        //   359: dup            
        //   360: aload           34
        //   362: aload           33
        //   364: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //   367: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   370: astore          35
        //   372: aload           35
        //   374: ifnull          493
        //   377: sipush          1024
        //   380: newarray        B
        //   382: astore          43
        //   384: aload           15
        //   386: aload           43
        //   388: invokevirtual   java/io/InputStream.read:([B)I
        //   391: istore          44
        //   393: iload           44
        //   395: iconst_m1      
        //   396: if_icmpeq       493
        //   399: aload           35
        //   401: aload           43
        //   403: iconst_0       
        //   404: iload           44
        //   406: invokevirtual   java/io/FileOutputStream.write:([BII)V
        //   409: goto            384
        //   412: astore          42
        //   414: aload           35
        //   416: astore          4
        //   418: aload           15
        //   420: astore          13
        //   422: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //   425: new             Ljava/lang/StringBuilder;
        //   428: dup            
        //   429: invokespecial   java/lang/StringBuilder.<init>:()V
        //   432: ldc             "File: "
        //   434: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   437: aload_1        
        //   438: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   441: ldc             " doesn't exist"
        //   443: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   446: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   449: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   452: pop            
        //   453: aload           13
        //   455: ifnull          206
        //   458: aload           13
        //   460: invokevirtual   java/io/InputStream.close:()V
        //   463: aload           4
        //   465: ifnull          206
        //   468: aload           4
        //   470: invokevirtual   java/io/FileOutputStream.close:()V
        //   473: aload_3        
        //   474: areturn        
        //   475: astore          24
        //   477: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //   480: ldc             "Unable to close out stream"
        //   482: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   485: pop            
        //   486: aload           24
        //   488: invokevirtual   java/io/IOException.printStackTrace:()V
        //   491: aload_3        
        //   492: areturn        
        //   493: aload           35
        //   495: invokevirtual   java/io/FileOutputStream.close:()V
        //   498: aload_3        
        //   499: ifnull          509
        //   502: aload_3        
        //   503: aload           30
        //   505: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   508: pop            
        //   509: aload           15
        //   511: ifnull          206
        //   514: aload           15
        //   516: invokevirtual   java/io/InputStream.close:()V
        //   519: aload           35
        //   521: ifnull          206
        //   524: aload           35
        //   526: invokevirtual   java/io/FileOutputStream.close:()V
        //   529: aload_3        
        //   530: areturn        
        //   531: astore          40
        //   533: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //   536: ldc             "Unable to close out stream"
        //   538: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   541: pop            
        //   542: aload           40
        //   544: invokevirtual   java/io/IOException.printStackTrace:()V
        //   547: aload_3        
        //   548: areturn        
        //   549: astore          38
        //   551: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //   554: ldc             "Unable to close in stream"
        //   556: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   559: pop            
        //   560: aload           38
        //   562: invokevirtual   java/io/IOException.printStackTrace:()V
        //   565: goto            519
        //   568: astore          22
        //   570: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //   573: ldc             "Unable to close in stream"
        //   575: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   578: pop            
        //   579: aload           22
        //   581: invokevirtual   java/io/IOException.printStackTrace:()V
        //   584: goto            463
        //   587: astore          16
        //   589: aconst_null    
        //   590: astore          15
        //   592: aload           15
        //   594: ifnull          612
        //   597: aload           15
        //   599: invokevirtual   java/io/InputStream.close:()V
        //   602: aload           4
        //   604: ifnull          612
        //   607: aload           4
        //   609: invokevirtual   java/io/FileOutputStream.close:()V
        //   612: aload           16
        //   614: athrow         
        //   615: astore          17
        //   617: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //   620: ldc             "Unable to close in stream"
        //   622: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   625: pop            
        //   626: aload           17
        //   628: invokevirtual   java/io/IOException.printStackTrace:()V
        //   631: goto            602
        //   634: astore          19
        //   636: getstatic       com/qualcomm/robotcore/util/ExtractAssets.a:Ljava/lang/String;
        //   639: ldc             "Unable to close out stream"
        //   641: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   644: pop            
        //   645: aload           19
        //   647: invokevirtual   java/io/IOException.printStackTrace:()V
        //   650: goto            612
        //   653: aload_1        
        //   654: ldc             ""
        //   656: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   659: ifne            680
        //   662: aload_1        
        //   663: getstatic       java/io/File.separator:Ljava/lang/String;
        //   666: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //   669: ifne            680
        //   672: aload_1        
        //   673: getstatic       java/io/File.separator:Ljava/lang/String;
        //   676: invokevirtual   java/lang/String.concat:(Ljava/lang/String;)Ljava/lang/String;
        //   679: astore_1       
        //   680: aload           8
        //   682: arraylength    
        //   683: istore          9
        //   685: iconst_0       
        //   686: istore          10
        //   688: iload           10
        //   690: iload           9
        //   692: if_icmpge       206
        //   695: aload_0        
        //   696: aload_1        
        //   697: aload           8
        //   699: iload           10
        //   701: aaload         
        //   702: invokevirtual   java/lang/String.concat:(Ljava/lang/String;)Ljava/lang/String;
        //   705: iload_2        
        //   706: aload_3        
        //   707: invokestatic    com/qualcomm/robotcore/util/ExtractAssets.a:(Landroid/content/Context;Ljava/lang/String;ZLjava/util/ArrayList;)Ljava/util/ArrayList;
        //   710: pop            
        //   711: iinc            10, 1
        //   714: goto            688
        //   717: astore          16
        //   719: aconst_null    
        //   720: astore          4
        //   722: goto            592
        //   725: astore          36
        //   727: aload           35
        //   729: astore          4
        //   731: aload           36
        //   733: astore          16
        //   735: goto            592
        //   738: astore          14
        //   740: aload           13
        //   742: astore          15
        //   744: aload           14
        //   746: astore          16
        //   748: goto            592
        //   751: astore          12
        //   753: aconst_null    
        //   754: astore          13
        //   756: aconst_null    
        //   757: astore          4
        //   759: goto            422
        //   762: astore          27
        //   764: aload           15
        //   766: astore          13
        //   768: aconst_null    
        //   769: astore          4
        //   771: goto            422
        //    Signature:
        //  (Landroid/content/Context;Ljava/lang/String;ZLjava/util/ArrayList<Ljava/lang/String;>;)Ljava/util/ArrayList<Ljava/lang/String;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  35     43     208    221    Ljava/io/IOException;
        //  53     61     751    762    Ljava/io/IOException;
        //  53     61     587    592    Any
        //  65     127    762    774    Ljava/io/IOException;
        //  65     127    717    725    Any
        //  131    137    762    774    Ljava/io/IOException;
        //  131    137    717    725    Any
        //  137    148    762    774    Ljava/io/IOException;
        //  137    148    717    725    Any
        //  152    188    762    774    Ljava/io/IOException;
        //  152    188    717    725    Any
        //  193    198    235    254    Ljava/io/IOException;
        //  202    206    254    272    Ljava/io/IOException;
        //  221    228    762    774    Ljava/io/IOException;
        //  221    228    717    725    Any
        //  272    352    762    774    Ljava/io/IOException;
        //  272    352    717    725    Any
        //  352    372    762    774    Ljava/io/IOException;
        //  352    372    717    725    Any
        //  377    384    412    422    Ljava/io/IOException;
        //  377    384    725    738    Any
        //  384    393    412    422    Ljava/io/IOException;
        //  384    393    725    738    Any
        //  399    409    412    422    Ljava/io/IOException;
        //  399    409    725    738    Any
        //  422    453    738    751    Any
        //  458    463    568    587    Ljava/io/IOException;
        //  468    473    475    493    Ljava/io/IOException;
        //  493    498    412    422    Ljava/io/IOException;
        //  493    498    725    738    Any
        //  502    509    412    422    Ljava/io/IOException;
        //  502    509    725    738    Any
        //  514    519    549    568    Ljava/io/IOException;
        //  524    529    531    549    Ljava/io/IOException;
        //  597    602    615    634    Ljava/io/IOException;
        //  607    612    634    653    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 354, Size: 354
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3305)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:114)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.doSaveJarDecompiled(ProcyonDecompiler.java:194)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.decompileToZip(ProcyonDecompiler.java:146)
        //     at the.bytecode.club.bytecodeviewer.gui.MainViewerGUI$18$1$2.run(MainViewerGUI.java:1093)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
