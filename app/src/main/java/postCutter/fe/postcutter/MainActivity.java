package postCutter.fe.postcutter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import postCutter.fe.postcutter.R;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private List<String> imagePaths;
    private RecyclerView imagesRV;
    private RecycleViewAdapter imageRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagePaths = new ArrayList<>();
        imagesRV = findViewById(R.id.main_RVImages);

        prepareRecyclerView();
        requestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();

        imagePaths.clear();
        getImagePath();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        if (checkPermission()) {
            getImagePath();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private void prepareRecyclerView() {
        imageRVAdapter = new RecycleViewAdapter(MainActivity.this, imagePaths);

        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 3);

        imagesRV.setLayoutManager(manager);
        imagesRV.setAdapter(imageRVAdapter);
    }

    private void getImagePath() {
        final String[] columns = {MediaStore.Images.Media.DATA};
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imagePaths.add(cursor.getString(dataColumnIndex));
        }
        imageRVAdapter.notifyDataSetChanged();
        cursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storageAccepted) {
                    Toast.makeText(this, getString(R.string.permission_granted_text), Toast.LENGTH_SHORT).show();
                    getImagePath();
                } else {
                    Toast.makeText(this, getString(R.string.permission_denied_text), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

}