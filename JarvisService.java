package com.jarvis.assistant;
import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.speech.*;
import java.util.*;
public class JarvisService extends Service {
    SpeechRecognizer recognizer;
    Map<String,String> appMap = new HashMap<String,String>() {{
        put("facebook","com.facebook.katana"); put("messenger","com.facebook.orca"); put("whatsapp","com.whatsapp"); put("instagram","com.instagram.android"); put("youtube","com.google.android.youtube"); put("free fire","com.dts.freefireth"); put("pubg","com.tencent.ig");
    }};
    public void onCreate() {
        super.onCreate();
        NotificationChannel ch = new NotificationChannel("jarvis","JARVIS",NotificationManager.IMPORTANCE_LOW);
        ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(ch);
        Notification n = new Notification.Builder(this,"jarvis").setContentTitle("JARVIS ON").setContentText("Sunche...").setSmallIcon(android.R.drawable.ic_btn_speak_now).build();
        startForeground(1,n);
        startListening();
    }
    void startListening() {
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD");
        recognizer.setRecognitionListener(new RecognitionListener() {
            public void onResults(Bundle b) { ArrayList<String> res = b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION); if(res!=null && !res.isEmpty()) handleVoice(res.get(0).toLowerCase()); startListening(); }
            public void onError(int e){ startListening(); }
            public void onReadyForSpeech(Bundle p){} public void onBeginningOfSpeech(){} public void onRmsChanged(float r){} public void onBufferReceived(byte[] b){} public void onEndOfSpeech(){} public void onPartialResults(Bundle p){} public void onEvent(int t, Bundle p){}
        });
        recognizer.startListening(intent);
    }
    void handleVoice(String txt) {
        if(txt.contains("jarvis")) { if(txt.contains("off")) return; txt=txt.replace("jarvis","").replace("on","").trim(); }
        if(txt.contains("message")||txt.contains("msg")) {
            String msg = txt.replace("message","").replace("msg","").replace("de","").replace("dao","").trim();
            ClipboardManager cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("JARVIS",msg));
            openApp("com.facebook.orca"); return;
        }
        if(txt.contains("call")||txt.contains("কল")) {
            String name = txt.replace("call","").replace("কল","").replace("re","").replace("de","").trim();
            callContact(name); return;
        }
        for(String key:appMap.keySet()) if(txt.contains(key)) { openApp(appMap.get(key)); return; }
    }
    void openApp(String pkg) { try { Intent launch = getPackageManager().getLaunchIntentForPackage(pkg); if(launch!=null){launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(launch);} } catch(Exception e){} }
    void callContact(String name) { try { Cursor c=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null); while(c!=null && c.moveToNext()){ String cName=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)); String cNum=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); if(cName!=null && cName.toLowerCase().contains(name.toLowerCase())){ Intent i=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+cNum)); i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(i); c.close(); return; }} if(c!=null) c.close(); } catch(Exception e){} }
    public IBinder onBind(Intent i){return null;}
}
