package app.sakura.momonga.kisotaionkanri

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.*

open class SaveModel (
    @PrimaryKey
    open var id: String = UUID.randomUUID().toString(),
    open var year: Int = 0,
    open var month: Int = 0,
    open var day: Int = 0,
    open var temperature: Float = 0F
) : RealmObject() {

}