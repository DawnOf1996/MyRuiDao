package com.lyp.myruidao.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.lyp.myruidao.R;
import com.lyp.myruidao.util.Constant;
import com.lyp.myruidao.util.SPUtils;
import com.lyp.networkhelper.activity.BaseActivity;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


public class LoginActivity extends BaseActivity {

    private EditText etName;
    private EditText etPassword;
    private EditText etCode;
    private CheckBox autoLogin;
    private CheckBox rememberMe;
    private ImageView imgCode;
    private Button btLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.login_main;
    }

    @Override
    protected void initViews() {
        etName = findView(R.id.et_name);
        etPassword = findView(R.id.et_password);
        etCode = findView(R.id.et_code);
        autoLogin = findView(R.id.autologin);
        rememberMe = findView(R.id.remember_me);
        imgCode = findView(R.id.img_code);
        btLogin = findView(R.id.bt_login);
    }

    @Override
    protected void initData() {
        refreshImgCode();

        String localUsername = (String) SPUtils.get(LoginActivity.this, "username", "");
        if (!localUsername.equals("")) {
            etName.setText(localUsername);
            rememberMe.setChecked(true);
        }
    }

    private void refreshImgCode() {
        OkHttpUtils
                .get()
                .url(Constant.URL_CODE_IMAGE)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        Map<String, Object> map = new HashMap<>();
                        map.put("Set-Cookie", response.headers().get("Set-Cookie"));
                        map.put("codeImg", BitmapFactory.decodeStream(response.body().byteStream()));
                        return map;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        imgCode.setImageResource(R.mipmap.ic_launcher);
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Map<String, Object> map = (Map<String, Object>) response;
                        imgCode.setImageBitmap((Bitmap) map.get("codeImg"));

                        String setcookie = (String) map.get("Set-Cookie");
                        if (setcookie != null) {
                            Logger.d(setcookie.substring(0, setcookie.indexOf(';')));
                            SPUtils.put(LoginActivity.this, "jsessionid", setcookie.substring(0, setcookie.indexOf(';')));
                        }
                    }
                });
    }


    @Override
    protected void initEvents() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInfo()) {
                    Map<String, String> map = new HashMap();
                    map.put("Cookie", (String) SPUtils.get(LoginActivity.this, "jsessionid", ""));

                    OkHttpUtils
                            .post()
                            .url(Constant.URL_LOGIN)
                            .headers(map)
                            .addParams("username", etName.getText().toString())
                            .addParams("pwd", etPassword.getText().toString())
                            .addParams("imgcode", etCode.getText().toString())
                            .build()
                            .execute(new Callback() {
                                @Override
                                public Object parseNetworkResponse(Response response, int id) throws Exception {
                                    return response.body().string();
                                }

                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    toast("连接失败，请退出后重试");
                                    Logger.e(e.getMessage());
                                }

                                @Override
                                public void onResponse(Object response, int id) {
                                    String json = (String) response;
                                    Logger.json(json);
                                    try {
                                        JSONObject object = new JSONObject(json);
                                        JSONObject loginReturn = object.getJSONObject("loginReturn");
                                        String msg = loginReturn.getString("msg");
                                        String flag = loginReturn.getString("loginFlag");

                                        if (flag.equals("0")) {
                                            toast(msg); //失败信息
                                            etPassword.setText("");
                                            etCode.setText("");
                                            refreshImgCode();
                                        } else {
                                            // toast("登录成功！");

                                            if (rememberMe.isChecked()) {
                                                SPUtils.put(LoginActivity.this, "username", etName.getText().toString());
                                            }

                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                            finish();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            }
        });

        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rememberMe.setChecked(true);
                } else {
                    rememberMe.setChecked(false);
                }
            }
        });

    }

    private boolean checkInfo() {
        etName.setError(null);
        etPassword.setError(null);
        etCode.setError(null);

        if (etName.getText().toString().equals("")) {
            etName.setError("请输入用户名");
            autoLogin.setChecked(false);
            rememberMe.setChecked(false);
            return false;
        }

        if (etPassword.getText().toString().equals("")) {
            etPassword.setError("请输入密码");
            autoLogin.setChecked(false);
            rememberMe.setChecked(false);
            return false;
        }

        if (etCode.getText().toString().equals("")) {
            etCode.setError("请输入验证码");
            autoLogin.setChecked(false);
            rememberMe.setChecked(false);
            return false;
        }
        return true;
    }
}
