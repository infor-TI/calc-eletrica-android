package br.com.inforti.calceletrica;
import android.content.*;import androidx.core.app.NotificationCompat;import androidx.core.app.NotificationManagerCompat;
public class ReminderReceiver extends BroadcastReceiver{
 public void onReceive(Context c,Intent i){String cliente=i.getStringExtra("cliente");String servico=i.getStringExtra("servico");NotificationCompat.Builder b=new NotificationCompat.Builder(c,"calc_eletrica_agenda").setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("Serviço amanhã").setContentText(cliente+" • "+servico).setStyle(new NotificationCompat.BigTextStyle().bigText("Cliente: "+cliente+"\nServiço: "+servico)).setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true);try{NotificationManagerCompat.from(c).notify(i.getIntExtra("id",1),b.build());}catch(SecurityException ignored){}}
}
