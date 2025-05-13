package org.turter.water_app_mobile.`data`.local.config

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass
import org.turter.water_app_mobile.`data`.local.dao.ConsumeWaterVolumeDao
import org.turter.water_app_mobile.`data`.local.dao.ConsumeWaterVolumeDao_Impl

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _consumeWaterVolumeDao: Lazy<ConsumeWaterVolumeDao> = lazy {
    ConsumeWaterVolumeDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "fe845d03347376579beedd2324931407", "5ace21832e024bbb72e047325a0936e8") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `consume_water_volumes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `volumeMl` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'fe845d03347376579beedd2324931407')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `consume_water_volumes`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsConsumeWaterVolumes: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsConsumeWaterVolumes.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConsumeWaterVolumes.put("volumeMl", TableInfo.Column("volumeMl", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysConsumeWaterVolumes: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesConsumeWaterVolumes: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoConsumeWaterVolumes: TableInfo = TableInfo("consume_water_volumes",
            _columnsConsumeWaterVolumes, _foreignKeysConsumeWaterVolumes,
            _indicesConsumeWaterVolumes)
        val _existingConsumeWaterVolumes: TableInfo = read(connection, "consume_water_volumes")
        if (!_infoConsumeWaterVolumes.equals(_existingConsumeWaterVolumes)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |consume_water_volumes(org.turter.water_app_mobile.data.local.entity.ConsumeWaterVolumeDbModel).
              | Expected:
              |""".trimMargin() + _infoConsumeWaterVolumes + """
              |
              | Found:
              |""".trimMargin() + _existingConsumeWaterVolumes)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "consume_water_volumes")
  }

  public override fun clearAllTables() {
    super.performClear(false, "consume_water_volumes")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ConsumeWaterVolumeDao::class,
        ConsumeWaterVolumeDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun consumeWaterVolumeDao(): ConsumeWaterVolumeDao = _consumeWaterVolumeDao.value
}
