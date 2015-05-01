package eu.alican.toolrental.utls;

/**
 * Project: ToolRental
 * Created by alican on 30.04.2015.
 */
import android.os.Build;

public final class AndroidUtils {
    public static boolean isLollipop() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}