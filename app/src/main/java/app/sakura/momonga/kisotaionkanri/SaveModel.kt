package app.sakura.momonga.kisotaionkanri

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.*

open class SaveModel (
    @PrimaryKey
    open var id: String = UUID.randomUUID().toString(),
    open var temperature: Int = 0,
    var date:Date = Date()
) : RealmObject() {}