package org.turter.water_app_mobile.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow
import org.turter.water_app_mobile.`data`.local.entity.ConsumeWaterVolumeDbModel

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ConsumeWaterVolumeDao_Impl(
  __db: RoomDatabase,
) : ConsumeWaterVolumeDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfConsumeWaterVolumeDbModel:
      EntityInsertAdapter<ConsumeWaterVolumeDbModel>
  init {
    this.__db = __db
    this.__insertAdapterOfConsumeWaterVolumeDbModel = object :
        EntityInsertAdapter<ConsumeWaterVolumeDbModel>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `consume_water_volumes` (`id`,`volumeMl`) VALUES (nullif(?, 0),?)"

      protected override fun bind(statement: SQLiteStatement, entity: ConsumeWaterVolumeDbModel) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.volumeMl.toLong())
      }
    }
  }

  public override suspend fun addVolume(volume: ConsumeWaterVolumeDbModel): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfConsumeWaterVolumeDbModel.insert(_connection, volume)
  }

  public override fun getAllVolumes(): Flow<List<ConsumeWaterVolumeDbModel>> {
    val _sql: String = "SELECT * FROM consume_water_volumes"
    return createFlow(__db, false, arrayOf("consume_water_volumes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfVolumeMl: Int = getColumnIndexOrThrow(_stmt, "volumeMl")
        val _result: MutableList<ConsumeWaterVolumeDbModel> = mutableListOf()
        while (_stmt.step()) {
          val _item: ConsumeWaterVolumeDbModel
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpVolumeMl: Int
          _tmpVolumeMl = _stmt.getLong(_columnIndexOfVolumeMl).toInt()
          _item = ConsumeWaterVolumeDbModel(_tmpId,_tmpVolumeMl)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteVolume(volumeId: Int) {
    val _sql: String = "DELETE FROM consume_water_volumes WHERE id =?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, volumeId.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
