package chat.rocket.android;

import android.app.Application;
import chat.rocket.android.model.ServerConfig;
import chat.rocket.android.realm_helper.RealmStore;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import java.util.List;
import timber.log.Timber;

/**
 * Customized Application-class for Rocket.Chat
 */
public class RocketChatApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();

    Timber.plant(new Timber.DebugTree());

    Realm.init(this);
    Realm.setDefaultConfiguration(
        new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build());

    List<ServerConfig> configs = RealmStore.getDefault().executeTransactionForReadResults(realm ->
        realm.where(ServerConfig.class).isNotNull("session").findAll());
    for (ServerConfig config : configs) {
      RealmStore.put(config.getServerConfigId());
    }

    Stetho.initialize(Stetho.newInitializerBuilder(this)
        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
        .build());

    //TODO: add periodic trigger for RocketChatService.keepalive(this) here!
  }
}