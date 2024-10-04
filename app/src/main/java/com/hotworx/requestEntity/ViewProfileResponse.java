//package com.hotworx.requestEntity;
//
//
//import com.google.gson.annotations.SerializedName;
//
//import java.io.Serializable;
//
//public class ViewProfileResponse extends BaseModel implements Serializable {
//
//    private String full_name;
//    private String first_name;
//    private String last_name;
//
//    private String user_id;
//    private String login_id;
//    private String country;
//    private String phone;
//    private String user_type;
//    private String img_src;
//    private String is_active;
//
//    private String email;
//    private String height;
//    private String weight;
//    private String dob;
//    private String gender;
//    private String address;
//    private String showpvt;
//    private String is_admin;
//    private String showdiettrax;
//
//    @SerializedName("error_message")
//    private String error;
//    @SerializedName("appointment_link")
//    private String appointmentLink;
//
//    public String getLogin_id() {
//        return login_id;
//    }
//
//    public void setLogin_id(String login_id) {
//        this.login_id = login_id;
//    }
//
//    public String getDietTrax() {
//        return showdiettrax;
//    }
//
//    public void setDietTrax(String show_dietrax) {
//        this.showdiettrax = show_dietrax;
//    }
//
//    public String getFull_name() {
//        return full_name;
//    }
//
//    public void setFull_name(String full_name) {
//        this.full_name = full_name;
//    }
//
//    public String getFirst_name() {
//        return first_name;
//    }
//
//    public void setFirst_name(String first_name) {
//        this.first_name = first_name;
//    }
//
//    public String getLast_name() {
//        return last_name;
//    }
//
//    public void setLast_name(String last_name) {
//        this.last_name = last_name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getHeight() {
//        return height;
//    }
//
//    public void setHeight(String height) {
//        this.height = height;
//    }
//
//    public String getWeight() {
//        return weight;
//    }
//
//    public void setWeight(String weight) {
//        this.weight = weight;
//    }
//
//    public String getDob() {
//        return dob;
//    }
//
//    public void setDob(String dob) {
//        this.dob = dob;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(String user_id) {
//        this.user_id = user_id;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//
//    public void setCountry(String country) {
//        this.country = country;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getUser_type() {
//        return user_type;
//    }
//
//    public void setUser_type(String user_type) {
//        this.user_type = user_type;
//    }
//
//    public String getImg_src() {
//        return img_src;
//    }
//
//    public void setImg_src(String img_src) {
//        this.img_src = img_src;
//    }
//
//    public String getIs_active() {
//        return is_active;
//    }
//
//    public void setIs_active(String is_active) {
//        this.is_active = is_active;
//    }
//
//    public String getShowpvt() {
//        return showpvt;
//    }
//
//    public void setShowpvt(String showpvt) {
//        this.showpvt = showpvt;
//    }
//
//    public String getIs_admin() {
//        return is_admin;
//    }
//
//    public void setIs_admin(String is_admin) {
//        this.is_admin = is_admin;
//    }
//
//    public String getAppointmentLink() {
//        return appointmentLink;
//    }
//
//    public void setAppointmentLink(String appointmentLink) {
//        this.appointmentLink = appointmentLink;
//    }
//    public String getError() {
//        return error;
//    }
//    public void setError(String error) {
//        this.error = error;
//    }
//}
//
//


package com.hotworx.requestEntity;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ViewProfileResponse extends BaseModel implements Serializable {

    private String access_camera;
    private String access_token_fitbit;
    private String address;
    private String android_push_id;
    private String apple_push_id;
    private String country;

    private String is_ambassador_allowed;
    private String is_hotsquad_enabled;
    private String is_passio_enabled;
    private String is_employee_allowed;

    public String getIsAmbassadorAllowed() {
        return is_ambassador_allowed;
    }

    public void setIsAmbassadorAllowed(String is_ambassador_allowed) {
        this.is_ambassador_allowed = is_ambassador_allowed;
    }

    public String getIs_hotsquad_enabled() {
        return is_hotsquad_enabled;
    }

    public void setIs_hotsquad_enabled(String is_hotsquad_enabled) {
        this.is_hotsquad_enabled = is_hotsquad_enabled;
    }


    public String getIs_passio_enabled() {
        return is_passio_enabled;
    }

    public void setIs_passio_enabled(String is_passio_enabled) {
        this.is_passio_enabled = is_passio_enabled;
    }


    public String getIsEmployeeAllowed() {
        return is_employee_allowed;
    }

    public void setIsEmployeeAllowed(String is_employee_allowed) {
        this.is_employee_allowed = is_employee_allowed;
    }
    public String getAccess_camera() {
        return access_camera;
    }

    public void setAccess_camera(String access_camera) {
        this.access_camera = access_camera;
    }

    public String getAccess_token_fitbit() {
        return access_token_fitbit;
    }

    public void setAccess_token_fitbit(String access_token_fitbit) {
        this.access_token_fitbit = access_token_fitbit;
    }

    public String getAndroid_push_id() {
        return android_push_id;
    }

    public void setAndroid_push_id(String android_push_id) {
        this.android_push_id = android_push_id;
    }

    public String getApple_push_id() {
        return apple_push_id;
    }

    public void setApple_push_id(String apple_push_id) {
        this.apple_push_id = apple_push_id;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getDb_type() {
        return db_type;
    }

    public void setDb_type(String db_type) {
        this.db_type = db_type;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getFranchise_count() {
        return franchise_count;
    }

    public void setFranchise_count(String franchise_count) {
        this.franchise_count = franchise_count;
    }

    public String getHeight_in_cm() {
        return height_in_cm;
    }

    public void setHeight_in_cm(String height_in_cm) {
        this.height_in_cm = height_in_cm;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getIntermittent_hr() {
        return intermittent_hr;
    }

    public void setIntermittent_hr(String intermittent_hr) {
        this.intermittent_hr = intermittent_hr;
    }

    public String getIntermittent_status() {
        return intermittent_status;
    }

    public void setIntermittent_status(String intermittent_status) {
        this.intermittent_status = intermittent_status;
    }

    public String getIs_reminder() {
        return is_reminder;
    }

    public void setIs_reminder(String is_reminder) {
        this.is_reminder = is_reminder;
    }

    public String getIs_vibration() {
        return is_vibration;
    }

    public void setIs_vibration(String is_vibration) {
        this.is_vibration = is_vibration;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMyaccount_url() {
        return myaccount_url;
    }

    public void setMyaccount_url(String myaccount_url) {
        this.myaccount_url = myaccount_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getRating_status() {
        return rating_status;
    }

    public void setRating_status(String rating_status) {
        this.rating_status = rating_status;
    }

    public String getShow_myaccount() {
        return show_myaccount;
    }

    public void setShow_myaccount(String show_myaccount) {
        this.show_myaccount = show_myaccount;
    }

    public String getShowdiettrax() {
        return showdiettrax;
    }

    public void setShowdiettrax(String showdiettrax) {
        this.showdiettrax = showdiettrax;
    }

//    @Override
//    public boolean isStatus() {
//        return status;
//    }
//
//    @Override
//    public void setStatus(boolean status) {
//        this.status = status;
//    }

    public String getWeight_in_kg() {
        return weight_in_kg;
    }

    public void setWeight_in_kg(String weight_in_kg) {
        this.weight_in_kg = weight_in_kg;
    }

    private String creation_date; // 2023-11-19 10:51:04
    private String db_type; // hotworx
    private String dob;
    private String email; // employee@emloyee.com
    private String email_address;
    private String first_name; // Employeeeee
    private String franchise_count; // 0
    private String full_name; // Employeeeee _
    private String gender;
    private String height;
    private String height_in_cm;
    private String image_url;
    private String img_src;
    private String intermittent_hr; // 0
    private String intermittent_status; // true
    private String is_active; // yes
    private String is_admin; // no
    private String is_reminder;
    private String is_vibration;
    private String last_name; // _
    private String location_id; // 0
    private String login_id;
    @SerializedName("login_message")
    private String message; // login successfully
    private String myaccount_url; // https://hotworx.net
    private String name; // Employeeeee _
    private String password; // test1234
    private String phone; // 5556667777
    private String phone_number;
    private String rating_status; // 0
    private String show_myaccount; // no
    private String showdiettrax; // yes
    private String showpvt; // yes
    @SerializedName("login_status")
    private boolean status; // true
    private String user_type;
    private String weight;
    private String weight_in_kg;
    private String user_id;



    @SerializedName("appointment_link")
    private String appointmentLink;
    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getDietTrax() {
        return showdiettrax;
    }


    public void setDietTrax(String show_dietrax) {
        this.showdiettrax = show_dietrax;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getShowpvt() {
        return showpvt;
    }

    public void setShowpvt(String showpvt) {
        this.showpvt = showpvt;
    }

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    public String getAppointmentLink() {
        return appointmentLink;
    }

    public void setAppointmentLink(String appointmentLink) {
        this.appointmentLink = appointmentLink;
    }

}


