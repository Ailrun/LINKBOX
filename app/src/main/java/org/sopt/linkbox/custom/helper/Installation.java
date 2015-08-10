package org.sopt.linkbox.custom.helper;

import android.content.Context;

import java.util.UUID;

/**
 * Created by Junyoung on 2015-08-08.
 */
public class Installation {
    public static String getDeviceUUID(final Context context) {
        final SharedPreferencesCache cache = Session.getAppCache();
        final String id = cache.getString(PROPERTY_DEVICE_ID);

        UUID uuid = null;
        if (id != null) {
            uuid = UUID.fromString(id);
        } else {
            final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            try {
                if (!"9774d56d682e549c".equals(androidId)) {
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                } else {
                    final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            Bundle bundle = new Bundle();
            bundle.putString(PROPERTY_DEVICE_ID, uuid.toString());
            cache.save(bundle);
        }

        return uuid.toString();
    }
}
