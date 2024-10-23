package com.example.helloworld

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ImageReceive : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var selectImageBtn: Button
    private lateinit var postIdInput: EditText
    private lateinit var targetAmountTextView: TextView
    private lateinit var navigateToMainBtn: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_receive)  // XMLレイアウトファイルを設定

        // UI要素の初期化
        imageView = findViewById(R.id.imageView)  // 画像を表示するImageView
        selectImageBtn = findViewById(R.id.selectImageBtn)  // 画像を選択するボタン
        postIdInput = findViewById(R.id.postIdInput)  // postIdを入力するEditText
        targetAmountTextView = findViewById(R.id.targetAmountTextView)  // 目標金額を表示するTextView
        navigateToMainBtn = findViewById(R.id.navigateToMainBtn)  // MainActivityに遷移するボタン

        // Firebase Realtime Databaseの参照を取得
        databaseReference = FirebaseDatabase.getInstance().getReference("posts")

        // 画像と目標金額を取得するためのボタンのクリックリスナー
        selectImageBtn.setOnClickListener {
            val postId = postIdInput.text.toString().trim()  // ユーザーが入力したPost IDを取得

            if (postId.isNotEmpty()) {
                // postIdを元にFirebase Realtime Databaseからデータを取得
                databaseReference.child(postId).get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        // 画像URLと目標金額を取得
                        val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)
                        val targetAmount = snapshot.child("targetAmount").getValue(Int::class.java)

                        // 画像URLが存在する場合、Picassoで画像をImageViewに表示
                        if (imageUrl != null) {
                            Picasso.get().load(imageUrl).into(imageView)
                        } else {
                            Toast.makeText(this, "画像のURLが存在しません", Toast.LENGTH_SHORT).show()
                        }

                        // 目標金額が存在する場合、TextViewに表示
                        if (targetAmount != null) {
                            targetAmountTextView.text = "目標金額: $targetAmount 円"
                        } else {
                            Toast.makeText(this, "目標金額が存在しません", Toast.LENGTH_SHORT).show()
                        }

                        // データの取得に成功したら、MainActivityに遷移するボタンを表示
                        navigateToMainBtn.visibility = Button.VISIBLE

                    } else {
                        // データが存在しない場合のエラーメッセージ
                        Toast.makeText(this, "指定されたPost IDのデータが見つかりません", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {exception ->
                    // データ取得に失敗した場合のエラーメッセージ
                    Toast.makeText(this, "データの取得に失敗しました: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Post IDが空の場合のエラーメッセージ
                Toast.makeText(this, "Post IDを入力してください", Toast.LENGTH_SHORT).show()
            }
        }

        // MainActivityへ遷移するボタンのクリックリスナー
        navigateToMainBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
