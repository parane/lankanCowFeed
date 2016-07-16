package cow.atuts.com.cowfeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import cow.atuts.com.cowfeed.dto.Result;

/**
 * Created by Parani on 16/07/2016.
 */
public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        Result result = (Result) getIntent().getSerializableExtra("MyClass");
        TextView  bulkForage = (TextView) findViewById(R.id.bulkForage);
        bulkForage.setText(result.getBulkForage());
        TextView  supplementaryForage = (TextView) findViewById(R.id.supplementaryForage);
        supplementaryForage.setText(result.getSupplementaryForage());
        TextView  Concentrate = (TextView) findViewById(R.id.Concentrate);
        Concentrate.setText(result.getConcentrate());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
