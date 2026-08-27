package br.com.inforti.calceletrica;

import android.app.*;
import android.os.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.webkit.*;
import android.graphics.Color;
import android.view.View;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    private WebView web;
    private static final String CHANNEL_ID="calc_eletrica_agenda";
    @Override public void onCreate(Bundle b){super.onCreate(b); criarCanal(); if(Build.VERSION.SDK_INT>=33&&checkSelfPermission("android.permission.POST_NOTIFICATIONS")!=PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(this,new String[]{"android.permission.POST_NOTIFICATIONS"},10);
        web=new WebView(this); web.setBackgroundColor(Color.rgb(9,9,9)); web.setWebViewClient(new WebViewClient()); WebSettings s=web.getSettings(); s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setDatabaseEnabled(true); s.setAllowFileAccess(true); s.setAllowContentAccess(true); s.setBuiltInZoomControls(false); s.setDisplayZoomControls(false); web.setOverScrollMode(View.OVER_SCROLL_NEVER); web.addJavascriptInterface(new AgendaBridge(this),"Android"); web.loadUrl("file:///android_asset/index.html"); setContentView(web); }
    private void criarCanal(){if(Build.VERSION.SDK_INT>=26){NotificationChannel c=new NotificationChannel(CHANNEL_ID,"Agenda Calc-Elétrica",NotificationManager.IMPORTANCE_HIGH);c.setDescription("Lembretes de serviços agendados");getSystemService(NotificationManager.class).createNotificationChannel(c);}}
    public static class AgendaBridge {Context ctx; AgendaBridge(Context c){ctx=c;} @JavascriptInterface public void agendarLembrete(long id,String cliente,String data,String hora,String servico){try{SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.US);Date d=f.parse(data+" "+hora);long trigger=d.getTime()-24L*60*60*1000; if(trigger<=System.currentTimeMillis()) trigger=System.currentTimeMillis()+60000; Intent i=new Intent(ctx,ReminderReceiver.class);i.putExtra("id",(int)(id%Integer.MAX_VALUE));i.putExtra("cliente",cliente);i.putExtra("servico",servico);PendingIntent pi=PendingIntent.getBroadcast(ctx,(int)(id%Integer.MAX_VALUE),i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);AlarmManager am=(AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,trigger,pi);}catch(Exception ignored){}} }
    @Override public void onBackPressed(){if(web!=null&&web.canGoBack())web.goBack();else super.onBackPressed();}
}
