package pl.tastycode.offliberator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by informatic on 28.04.14.
 */
public class ShareActivity extends Activity {
    private static final String TAG = "share";

    private Pattern urlPattern = Pattern.compile("\\b(((ht|f)tp(s?)\\:\\/\\/)|www.)" +
            "(\\w+:\\w+@)?(([-\\w]+\\.)+([a-z]{2,63}))(:[\\d]{1,5})?" + // yay for new tlds!
            "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" +
            "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
            "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" +
            "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
            "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" +
            "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b"); //"/\\bhttps?://\\S+/g");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        String intentText = intent.getStringExtra(Intent.EXTRA_TEXT);

        if (Intent.ACTION_SEND.equals(action) && type != null && intentText != null) {
            Log.i(TAG, type);
            Log.i(TAG, intentText);
            Matcher textUrlMatcher = urlPattern.matcher(intentText);

            // @TODO: Right now we only use the first URL, how about some ListView?
            if(textUrlMatcher.find()) {
                String url = textUrlMatcher.group();
                Uri targetUri = Uri.parse(getString(R.string.fmt_target_url, Uri.encode(url)));
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, targetUri);
                Log.i(TAG, "Opening " + targetUri.toString());
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, getString(R.string.msg_no_url), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.msg_no_url), Toast.LENGTH_LONG).show();
        }

        finish();
    }
}
