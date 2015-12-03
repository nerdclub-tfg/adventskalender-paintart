package de.harry_r.adventskalender;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.harry_r.adventskalender.Utils.Utils;

public class MainActivity extends AppCompatActivity {

    private int[] images = {
            R.drawable.zimtstern,
            R.drawable.kerze,
            R.drawable.kugel,
            R.drawable.stern,
            R.drawable.stiefel,
            R.drawable.stern_blau,
            R.drawable.unicorn,
            R.drawable.schlitten,
            R.drawable.weihnachtsmann
    };

    private Boolean[] opened_doors = new Boolean[24];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        for (int i=1; i<=24; i++) {
            opened_doors[i-1] = settings.getBoolean("openedDoors" + i, false);
            if (opened_doors[i-1]) {
                // get button id
                Button button = (Button) findViewById(getResources().getIdentifier("tor" + i, "id",
                        this.getPackageName()));
                //open door
                openDoor(i, button);
            }
        }
    }

    protected void onStop(){
        super.onStop();
        //save settings
        SharedPreferences settings = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        //creating a editor and add variables
        SharedPreferences.Editor editor = settings.edit();
        // can't add an array, so have to iterate through the array and save every single value
        for (int i=1; i<=24; i++) {
            editor.putBoolean("openedDoors" + i, opened_doors[i-1]);
        }
        // Commit the edits!
        editor.apply();
    }

    private void doDoorAction(int number, Button door) {
        switch (Utils.checkDate(number)) {
            case 0:
                Utils.showCommentTooEarly(this);
                break;
            case 1:
                openDoor(number, door);
                break;
            case 2:
                Utils.showCommentTooLate(this);
                openDoor(number, door);
                break;

        }
    }

    private void openDoor(int number, Button door) {
        door.setText("");
        door.setBackgroundResource(images[number-1]);
        door.setEnabled(false);
        opened_doors[number-1] = true;
    }


    public void clickDoor(View view) {
        Button door = (Button) view;
        int number = Integer.parseInt(door.getText().toString());
        doDoorAction(number, door);
    }

}
