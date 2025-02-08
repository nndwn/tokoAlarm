package com.example.tokoalarm

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class LoginResponse(
    val status: Boolean,
    val message: String,
    val data: UserData
)

data class RegisterResponse(
    val status: Boolean,
    val message: String,
    val data: UserData?
)

data class UserData(
    val id: String,
    val nama: String,
    val nohp: String,
    val password: String
)

data class DataPelangganResponse(
    val status: Boolean,
    val saldo: String,
    val data: List<ListPromo>,
    val config: Config
)

//Todo: Link Tutorial tidak sesuai pada API diberikan
data class Config(
    @SerializedName("link_tutorial")
    val linkTutorial: String,
    @SerializedName("link_pesan_alarm")
    val linkPesanAlarm: String
)

//Todo: Pada list Promo tidak terdapat link di API
data class ListPromo(
    @SerializedName("updated_at")
    val updatedAt: String,
    val banner: String,
    @SerializedName("created_at")
    val createdAt: String,
    val id: String,
    val deskripsi: String,
    val title: String
)

data class TopUpResponse(
    val status: Boolean,
    val data: List<ListTopUpData>
)

data class ListTopUpData(
    val keterangan: String,
    val noreff: String,
    val jumlah: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("id_users")
    val idUsers: String,
    @SerializedName("created_at")
    val cratedAt: String,
    val id: String,
    @SerializedName("saldo_awal")
    val saldoAwal: String,
    val tipe: String,
    @SerializedName("saldo_akhir")
    val saldoAkhir: String,
    val status: String
)


data class BankAccount(
    val id: Int,
    val name: String,
    val numberRek: String,
    val owner: String
)

data class Price(
    val id: Int,
    val price: Int
)

data class Tone(
    val name: String,
    val value: Int
)

data class PaketResponse(
    val status: Boolean,
    val data: List<ListPaket>
)

data class ListPaket(
    val biaya: String,
    @SerializedName("updated_at")
    val updateAt: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("biaya_rupiah")
    val biayaRupiah: String,
    val id: String,
    @SerializedName("cutoff_day")
    val cutoffDay: String,
    val periode: String,
    @SerializedName("day_convertion")
    val dayConvertion: String
)

data class ListAlatResponse(
    val status: Boolean,
    val data: List<ListAlat>
)

data class ListAlat(
    @SerializedName("nama_paket")
    var namePaket: String,
    @SerializedName("nomor_paket")
    val nomorPaket: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("biaya_rupiah")
    val biayaRupiah: String,
    @SerializedName("cutoff_day")
    val cutoffDay: String,
    val periode: String,
    @SerializedName("tanggal_selesai")
    val tanggalSelesai: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val biaya: String,
    @SerializedName("tanggal_mulai")
    val tanggalMulai: String,
    @SerializedName("id_alat")
    var idAlat: String,
    val nohp: String,
    val id: String,
    @SerializedName("sisa_hari")
    val sisaHari: String,
    val status: String,
    @SerializedName("day_convertion")
    val dayConvertion: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(namePaket)
        parcel.writeString(nomorPaket)
        parcel.writeString(createdAt)
        parcel.writeString(biayaRupiah)
        parcel.writeString(cutoffDay)
        parcel.writeString(periode)
        parcel.writeString(tanggalSelesai)
        parcel.writeString(updatedAt)
        parcel.writeString(biaya)
        parcel.writeString(tanggalMulai)
        parcel.writeString(idAlat)
        parcel.writeString(nohp)
        parcel.writeString(id)
        parcel.writeString(sisaHari)
        parcel.writeString(status)
        parcel.writeString(dayConvertion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListAlat> {
        override fun createFromParcel(parcel: Parcel): ListAlat {
            return ListAlat(parcel)
        }

        override fun newArray(size: Int): Array<ListAlat?> {
            return arrayOfNulls(size)
        }
    }
}

data class ListJadwalResponse(
    val status: Int,
    val data: List<ListJadwal>
)

data class ListJadwal(
    val id: String,
    @SerializedName("id_book")
    val idBook: String,
    val name: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    val days: String,
    @SerializedName("is_active")
    val isActive: String,
    @SerializedName("sensor_switch")
    val sensorSwitch: String,
    @SerializedName("sensor_ohm")
    val sensorOhm: String,
    @SerializedName("sensor_rf")
    val sensorRf: String,
    @SerializedName("nama_paket")
    val namePaket: String,
    @SerializedName("id_alat")
    val idAlat: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(idBook)
        parcel.writeString(name)
        parcel.writeString(startTime)
        parcel.writeString(endTime)
        parcel.writeString(days)
        parcel.writeString(isActive)
        parcel.writeString(sensorSwitch)
        parcel.writeString(sensorOhm)
        parcel.writeString(sensorRf)
        parcel.writeString(namePaket)
        parcel.writeString(idAlat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListJadwal> {
        override fun createFromParcel(parcel: Parcel): ListJadwal {
            return ListJadwal(parcel)
        }

        override fun newArray(size: Int): Array<ListJadwal?> {
            return arrayOfNulls(size)
        }
    }
}

data class ListNotJadwalResponse(
    val status: Int,
    val data: List<ListAddJadwal>
)

data class ListAddJadwal(
    val id: String,
    @SerializedName("id_alat")
    val idAlat: String,
    @SerializedName("nama_alat")
    val namaAlat: String,
    @SerializedName("tanggal_mulai")
    val tanggalMulai: String,
    @SerializedName("tanggal_selesai")
    val tanggalSelesai: String,
    @SerializedName("sisa_hari")
    val sisaHari: String
)

data class ResponseSettingList (
    val status :Boolean,
    @SerializedName("last_alat")
    val lastAlat : LastAlat
)

data class LastAlat(
    val status : Boolean,
    val data : ListSetting
)

data class ListSetting(
    val id: String,
    @SerializedName("id_alat")
    val idAlat: String,
    val mode : String,
)


