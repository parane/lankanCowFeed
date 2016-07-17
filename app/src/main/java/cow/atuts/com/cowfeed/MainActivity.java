package cow.atuts.com.cowfeed;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import cow.atuts.com.cowfeed.dto.Result;
import cow.atuts.com.cowfeed.staticData.DataGenerator;
import cow.atuts.com.cowfeed.staticData.DataGeneratorHelper;

public class MainActivity extends AppCompatActivity  implements
        MultiSpinner.MultiSpinnerListener {
    MultiSpinner multiSpinner1,multiSpinner2,multiSpinner3;
    EditText weight,milk,fat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        multiSpinner1= (MultiSpinner) findViewById(R.id.BulkForage);
        List<String> bulkFo = new ArrayList<String>(DataGeneratorHelper.getFeedNutritionCatOne().keySet());

        multiSpinner1.setItems(bulkFo, "BulkForage", this);
        multiSpinner2= (MultiSpinner) findViewById(R.id.Supplementary);
        List<String> sub = new ArrayList<String>(DataGeneratorHelper.getFeedNutritionCatTwo().keySet());

        multiSpinner2.setItems(sub, "Supplementary", this);
        multiSpinner3= (MultiSpinner) findViewById(R.id.Forage);
        List<String> foer = new ArrayList<String>(DataGeneratorHelper.getFeedNutritionCatThree().keySet());

        multiSpinner3.setItems(foer, "Forage", this);

        weight = (EditText) findViewById(R.id.input_name);
        milk = (EditText) findViewById(R.id.input_email);
        fat = (EditText) findViewById(R.id.input_password);

    }


    public void onSubmit(View v) {

        Double cowweight =Double.parseDouble(weight.getText().toString().trim());
        Double milkper =Double.parseDouble(milk.getText().toString().trim());
        Double fatpe =Double.parseDouble(fat.getText().toString().trim());

        List<String> firstlist = getStringValue(multiSpinner1.getSelectedItems(), new ArrayList<String>(DataGeneratorHelper.getFeedNutritionCatOne().keySet()));
        List<String> seclist = getStringValue(multiSpinner2.getSelectedItems(), new ArrayList<String>(DataGeneratorHelper.getFeedNutritionCatTwo().keySet()));
        List<String> threlist = getStringValue(multiSpinner3.getSelectedItems(), new ArrayList<String>(DataGeneratorHelper.getFeedNutritionCatThree().keySet()));


        Result result = DataGenerator.calculate(cowweight,milkper,fatpe,firstlist,seclist,threlist);

      /*  Result result =new Result();
        result.setConcentrate("dskdnskd");
        result.setSupplementaryForage("sdskdnskd");

         // Input
        List<String> selectedBulkForage = new ArrayList<>(Arrays.asList("co3","gunia"));

        //Input
        List<String> selectedSupplementary = new ArrayList<>(Arrays.asList("gliricidia","albezia"));

        //Input
        List<String> concentrateForage = new ArrayList<>(Arrays.asList("coconutpoonac"));
        result.setBulkForage("sdskdnskd");*/
        Intent in = new Intent(MainActivity.this,ResultActivity.class);

        in.putExtra("MyClass", result);
        startActivity(in);

        Log.d("parani","parani"+result.getBulkForage());
    }

    public List<String> getStringValue(boolean[] selected,List<String> list){
        List<String> selectedString=new ArrayList<>();

        for(int i=0; i<selected.length;i++){
            if(selected[i]==true){
                selectedString.add(list.get(i));
            }

        }
        return selectedString;
    }

    @Override
    public void onItemsSelected(boolean[] selected) {

    }
}
