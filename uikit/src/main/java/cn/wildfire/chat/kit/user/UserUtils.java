package cn.wildfire.chat.kit.user;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import com.lqr.imagepicker.ImagePicker;

public class UserUtils {

    public static final int REQUEST_CODE_PICK_IMAGE = 100;

    public static void portrait(Activity activity) {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            };
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (activity.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(permissions, 100);
                    return;
                }
            }
        }
        ImagePicker.picker().pick(activity, REQUEST_CODE_PICK_IMAGE);
    }
}
