package com.dawn.upgrade;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * 服务端检验逻辑的默认实现
 * @author dawn
 */
public class ServerCheckObserver extends HttpUpgradeObserver {
    /**
     * 构造方法
     * @param context 上下文
     * @param versionName 版本名称 如 V1.0.0， V2.2.8 或者其它字符串
     * @param versionCode 版本比较的数字 如 1,2
     * @param channel 来源（渠道号）
     * @param checkUrl 检查版本更新的地址
     * @param showDialog 是否显示升级提示对话框
     */
    public ServerCheckObserver(Context context, String versionName, int versionCode, String channel, String checkUrl, boolean showDialog) {
        super(context, versionName, versionCode, channel, checkUrl, showDialog);
    }

    @Override
    public CheckResult doCheck(String versionString) {
        CheckResult result = new CheckResult();
        if ("cancel".equals(versionString)) {
            result.setCheckResult(RESULT_CANCEL);
            return result;
        }
        try {
            JSONObject jsonObject = new JSONObject(versionString);
            boolean needUpgrade = jsonObject.optBoolean("upgrade", false);
            if (needUpgrade) {
                result.setCheckResult(RESULT_YES);
                result.setVersionCode(jsonObject.optInt("vc", 0));
                result.setVersionName(jsonObject.optString("vn", ""));
                result.setVersionInfo(jsonObject.optString("vi", ""));
                result.setForceVersion(jsonObject.optInt("fv", 0));
                result.setUrl(jsonObject.optString("url", ""));
                result.setApkSize(jsonObject.optInt("as", 0));
            } else {
                result.setCheckResult(RESULT_NO);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            result.setCheckResult(RESULT_ERROR_DATA);
        }
        return result;
    }

}