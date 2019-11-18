package app.sakura.momonga.kisotaionkanri

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.*

open class SaveModel (
    @PrimaryKey
    open var id: Int = 0,
    open var temperature: Int = 0
    //open var date:Date = Date()
) : RealmObject() {}