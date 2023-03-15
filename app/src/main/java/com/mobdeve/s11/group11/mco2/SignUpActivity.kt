package com.mobdeve.s11.group11.mco2

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s11.group11.mco2.databinding.ActivitySignupBinding


class SignUpActivity : AppCompatActivity() {
    private fun isEmpty(text: EditText): Boolean {
        val str: CharSequence = text.text.toString()
        return TextUtils.isEmpty(str)
    }
    private fun isEmail(text: EditText): Boolean {
        val email: CharSequence = text.text.toString()
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding : ActivitySignupBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        var et_email = findViewById<EditText>(R.id.et_signup_email)
        var et_firstName = findViewById<EditText>(R.id.et_signup_firstName)
        var et_lastName = findViewById<EditText>(R.id.et_signup_lastName)
        var et_weight = findViewById<EditText>(R.id.et_signup_weight)
        var et_password = findViewById<EditText>(R.id.et_signup_password)

        var tv_login = findViewById<TextView>(R.id.login_link)

        //SignUp Button set data of user and go to the Login Page and Activity
        viewBinding.signupBtn.setOnClickListener {
//            var email: String = et_email.text.toString()
//            var firstName: String  = et_firstName.text.toString()
//            var lastName: String  = et_lastName.text.toString()
//            var weightStr: String = et_weight.text.toString()
//            var weight: Int = weightStr.toInt()
//            var password : String = et_password.text.toString()

            var isTrue = true

            if(isEmpty(et_email) || isEmpty(et_firstName) || isEmpty(et_lastName) || isEmpty(et_weight) || isEmpty(et_password)){
                Toast.makeText(this@SignUpActivity, "Please Fill All Blanks", Toast.LENGTH_SHORT).show()
                isTrue = false
            }

            if(!isEmail(et_email)){
                Toast.makeText(this@SignUpActivity, "Invalid Email", Toast.LENGTH_SHORT).show()
                isTrue = false
            }

            if(isTrue){
                val signupIntent : Intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(signupIntent)

//                val user = User(firstName, lastName, weight, email, password)
//                UserData.loadUser().add(user)

                finish()
            }
        }

        var isHidden = true
        viewBinding.signupShowHideBtn.setOnClickListener{
            isHidden = if(isHidden){
                viewBinding.etSignupPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                viewBinding.signupShowHideBtn.setBackgroundResource(R.drawable.baseline_visibility_24)
                false
            } else {
                viewBinding.etSignupPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                viewBinding.signupShowHideBtn.setBackgroundResource(R.drawable.baseline_visibility_off_24)
                true
            }
        }

        //Login link to go to Login page and activity
        tv_login.setOnClickListener {
            val loginIntent : Intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }
}