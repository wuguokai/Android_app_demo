package com.example.wuguokai.firstapp.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WUGUOKAI on 2017/11/3.
 */

public class Util {
    /**
     * 判断是否登陆显示toast
     */
   /* public static boolean isLogin(Context context) {
        return isLogin(context, true);
    }*/

    /**
     * 判断是否登陆,是否显示toast
     */
   /* public static boolean isLogin(Context context, boolean isShow) {
        boolean isLogin = !isEmpty(UserUtils.getMyString(context, Constant.BundleKey.USER_ID));
        if (isShow && !isLogin) {
            ToastUtils.show(context, "请登录");
        }
        return isLogin;
    }*/

    /**
     * 将String 转换成Int 如果空或者异常则返回0
     */
    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    /**
     * 将String 转换成Int 如果空或者异常则返回0
     *
     * @param defValue 转换异常返回的数据
     */
    public static int parseInt(String str, int defValue) {
        if (isEmpty(str))
            return defValue;
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defValue;
        }
    }


    /**
     * 判断是否为空：（null:不区分大小写； ""）
     *
     * @return true:空，false:不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("") || str.trim().equalsIgnoreCase("null");
    }

    /**
     * 手机验证
     *
     * @param strPhone 手机号
     * @return boolean 检验结果 true:验证通过 false: 不通过
     */
    public static boolean isCellphone(String strPhone) {
        if (isEmpty(strPhone))
            return false;
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(strPhone);
        return matcher.matches();
    }


    /**
     * com.tencent.mm微信
     * com.sina.weibo新浪微博
     * 判断是否安装了QQ "com.tencent.mobileqq"
     * true 安装了相应包名的app
     */
    public static boolean hasApp(Context context, String packName) {
        boolean is = false;
        List<PackageInfo> packages = context.getPackageManager()
                .getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            String packageName = packageInfo.packageName;
            if (packageName.equals(packName)) {
                is = true;
            }
        }
        return is;
    }


    /**
     * 进入微信
     */
    public static void to_Wei_Xin(Context context) {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
    }

    /**
     * 时间格式化
     *
     * @param dateStr String 时间字符串
     * @param format  转换格式
     */
    public static Date strToDate(String dateStr, String format) {
        Date date = null;
        try {
            long timeLong = Long.parseLong(dateStr);
            date = new Date(timeLong);
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            String time = df.format(date);
            date = df.parse(time);
            return date;

        } catch (NumberFormatException e) {
            e.printStackTrace();
            if (!isEmpty(dateStr)) {
                DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
                try {
                    date = df.parse(dateStr);
                } catch (ParseException ea) {
                    ea.printStackTrace();
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


    /**
     * 用浏览器打开指定网址
     */
    public static void openBrowser(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "需要打开的地址不正确\n" + url, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号, 返回-1说明请求错误
     */
    public static double getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }


    /**
     * 背景变亮
     */
    public static void setBackAlphaHight(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 1.0f;
        activity.getWindow().setAttributes(params);
    }

    /**
     * 背景变暗
     */
    public static void setBackAlphaLow(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.5f;
        activity.getWindow().setAttributes(params);
    }

    /**
     * 关闭加载提示
     */
   /* public static void disLoaddingDialog(Context context, MProgressDialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
            setBackAlphaHight((Activity) context);
        }
    }*/

    /**
     * 可以封装的加载提示
     */
   /* public static void showLoaddingDialog(final Context context, final MProgressDialog dialog) {
        if (dialog != null) {
            setBackAlphaLow((Activity) context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show("正在加载");
        }
    }*/

    /**
     * 获取状态栏的高度
     *
     * @param activity
     * @return
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (statusHeight == 0) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object newInstance = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(newInstance).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * size[1] 高度
     * size[0] 宽度
     *
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        int res;
        if (dpValue < 0) {
            res = (int) (dpValue * scale - 0.5f);
            return res;
        }
        res = (int) (dpValue * scale + 0.5f);
        return res;
    }

    /**
     * 日期转换string
     *
     * @param date
     * @return
     */
    public static String getTimeDate2String(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 获取sHA1
     */
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置状态栏透明
     *
     * @param on
     */
    public static void setTranslucentStatus(boolean on, Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 显示右图标
     */
    public static void setTextIcon(Context context, TextView tv, Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            tv.setCompoundDrawables(null, null, drawable, null);
        } else {
            tv.setCompoundDrawables(null, null, null, null);
        }
    }

    /**
     * 显示左图标
     */
    public static void setTextIconLeft(TextView tv, Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            tv.setCompoundDrawables(drawable, null, null, null);
        } else {
            tv.setCompoundDrawables(null, null, null, null);
        }
    }

    //删除文件内容
    public static void clearCash(final String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }
}
