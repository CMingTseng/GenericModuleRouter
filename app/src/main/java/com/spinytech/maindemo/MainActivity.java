package com.spinytech.maindemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.spinytech.macore.ParceableAttachObject;
import com.spinytech.macore.RouterCallback;
import com.spinytech.macore.RouterManager;
import com.spinytech.macore.RouterRequest;
import  static  com.spinytech.maindemo.MyApplication.*;

public class MainActivity extends AppCompatActivity {



    private void toastOnUIThread(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_local_sync_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.getInstance()
                        .route(MainActivity.this, RouterRequest.obtain(MainActivity.this).provider("main")
                                        .action("sync")
                                        .data("1", "Hello")
                                        .data("2", "World"),
                                new RouterCallback() {
                                    @Override
                                    public void onResult(int resultCode, Bundle resultData) {
                                        String result = "";
                                        if(resultCode == RouterCallback.CODE_SUCCESS && resultData!=null){
                                            result =  resultData.getString(RouterCallback.KEY_VALUE);
                                        } else {
                                            result = resultData.getString(RouterCallback.KEY_ERROR_MSG);
                                        }
                                        toastOnUIThread(resultCode + "\t" + result);
                                    }
                                });

            }
        });
        findViewById(R.id.main_local_async_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.getInstance()
                        .route(MainActivity.this, RouterRequest.obtain(MainActivity.this).provider("main")
                                        .action("async")
                                        .data("1", "Hello")
                                        .data("2", "World"),
                                new RouterCallback() {
                                    @Override
                                    public void onResult(int resultCode, Bundle resultData) {
                                        String result = "";
                                        if(resultCode == RouterCallback.CODE_SUCCESS && resultData!=null){
                                            result =  resultData.getString(RouterCallback.KEY_VALUE);
                                        } else {
                                            result = resultData.getString(RouterCallback.KEY_ERROR_MSG);
                                        }
                                        toastOnUIThread(resultCode + "\t" + result);
                                    }
                                });
            }
        });

        findViewById(R.id.main_play_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long startTime = System.currentTimeMillis();
                final RouterRequest request = RouterRequest.obtain(MainActivity.this)
                        .domain(MUSIC_DOMAIN)
                        .provider("music")
                        .action("play");
                RouterManager.getInstance()
                        .route(MainActivity.this, request,
                                new RouterCallback() {
                                    @Override
                                    public void onResult(int resultCode, Bundle resultData) {
                                        String result = "";
                                        if(resultCode == RouterCallback.CODE_SUCCESS && resultData!=null){
                                            result =  resultData.getString(RouterCallback.KEY_VALUE);
                                        } else {
                                            result = resultData.getString(RouterCallback.KEY_ERROR_MSG);
                                        }
                                        toastOnUIThread(resultCode + "\t" + result);
                                    }
                                });
            }
        });
        findViewById(R.id.main_stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long startTime = System.currentTimeMillis();
                RouterManager.getInstance()
                        .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                        .domain(MUSIC_DOMAIN)
                                        .provider("music")
                                        .action("stop"),
                                new RouterCallback() {
                                    @Override
                                    public void onResult(int resultCode, Bundle resultData) {
                                        String result = "";
                                        if(resultCode == RouterCallback.CODE_SUCCESS && resultData!=null){
                                            result =  resultData.getString(RouterCallback.KEY_VALUE);
                                        } else {
                                            result = resultData.getString(RouterCallback.KEY_ERROR_MSG);
                                        }
                                        toastOnUIThread(resultCode + "\t" + result);
                                    }
                                });

            }
        });

        findViewById(R.id.main_music_shutdown_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long startTime = System.currentTimeMillis();
                RouterManager.getInstance()
                        .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                        .domain(MUSIC_DOMAIN)
                                        .provider("music")
                                        .action("shutdown"),
                                new RouterCallback() {
                                    @Override
                                    public void onResult(int resultCode, Bundle resultData) {
                                        String result = "";
                                        if(resultCode == RouterCallback.CODE_SUCCESS && resultData!=null){
                                            result =  resultData.getString(RouterCallback.KEY_VALUE);
                                        } else {
                                            result = resultData.getString(RouterCallback.KEY_ERROR_MSG);
                                        }
                                        toastOnUIThread(resultCode + "\t" + result);
                                    }
                                });
            }
        });
        findViewById(R.id.main_wide_shutdown_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        findViewById(R.id.main_pic_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long startTime = System.currentTimeMillis();
                RouterManager.getInstance()
                        .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                .domain(PIC_DOMAIN)
                                .provider("pic")
                                .action("pic")
                                .data("is_big", "0"), new RouterCallback() {
                            @Override
                            public void onResult(int resultCode, Bundle resultData) {
                                String result = "";
                                if(resultCode == RouterCallback.CODE_SUCCESS && resultData!=null){
                                    result = resultData.getString(RouterCallback.KEY_VALUE);
                                } else {
                                    result = resultData.getString(RouterCallback.KEY_ERROR_MSG);
                                }
                                toastOnUIThread(resultCode + "\t" + result);
                            }
                        });
            }
        });

        findViewById(R.id.main_big_pic_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long startTime = System.currentTimeMillis();
                RouterManager.getInstance()
                        .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                        .domain(PIC_DOMAIN)
                                        .provider("pic")
                                        .action("pic")
                                        .data("is_big", "1"),
                                new RouterCallback() {
                                    @Override
                                    public void onResult(int resultCode, Bundle resultData) {
                                        String result = "";
                                        if(resultCode == RouterCallback.CODE_SUCCESS && resultData!=null){
                                            result =  resultData.getString(RouterCallback.KEY_VALUE);
                                        } else {
                                            result = resultData.getString(RouterCallback.KEY_ERROR_MSG);
                                        }
                                        toastOnUIThread(resultCode + "\t" + result);
                                    }
                                });
            }
        });
        findViewById(R.id.main_web_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.getInstance()
                        .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                        .domain(WEB_DOMAIN)
                                        .provider("web")
                                        .action("web"),
                                new RouterCallback() {
                                    @Override
                                    public void onResult(int resultCode, Bundle resultData) {
                                        String result = "";
                                        if(resultCode == RouterCallback.CODE_SUCCESS && resultData!=null){
                                            result =  resultData.getString(RouterCallback.KEY_VALUE);
                                        } else {
                                            result = resultData.getString(RouterCallback.KEY_ERROR_MSG);
                                        }
                                        toastOnUIThread(resultCode + "\t" + result);
                                    }
                                }
                        );
            }
        });
        findViewById(R.id.main_attach_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.getInstance()
                        .route(MainActivity.this, RouterRequest.obtain(MainActivity.this)
                                        .provider("main")
                                        .action("attachment")
                                        .data("textview",findViewById(R.id.main_attach_btn)),
                                new RouterCallback() {
                                    @Override
                                    public void onResult(int resultCode, Bundle resultData) {
                                        String result = "";
                                        if(resultCode == RouterCallback.CODE_SUCCESS && resultData!=null){
                                            result = (String) resultData.get(RouterCallback.KEY_VALUE);
                                            final Object attachObj = resultData.get("toast");
                                            if( attachObj!= null && attachObj instanceof ParceableAttachObject){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ((Toast)((ParceableAttachObject) attachObj).obj).show();
                                                    }
                                                });
                                            }
                                        } else {
                                            result = (String)resultData.get(RouterCallback.KEY_ERROR_MSG);
                                        }
                                        toastOnUIThread(resultCode + "\t" + result);
                                    }
                                }
                        );

            }
        });
    }

}
