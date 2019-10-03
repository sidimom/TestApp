package my.test.testapp.myBinding;

import android.view.View;
import android.widget.Toast;

import my.test.testapp.R;

public class MyHandler {

    public void onAdd(View view){
        Toast.makeText(view.getContext(), "on Add", Toast.LENGTH_SHORT).show();
    }

    public void onDelete(View view){
        Toast.makeText(view.getContext(),"on Delete", Toast.LENGTH_SHORT).show();
    }
}
