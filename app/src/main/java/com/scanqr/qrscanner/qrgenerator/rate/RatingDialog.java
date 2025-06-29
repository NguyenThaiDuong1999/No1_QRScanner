package com.scanqr.qrscanner.qrgenerator.rate;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scanqr.qrscanner.qrgenerator.R;

public class RatingDialog extends Dialog {
    private final TextView tvTitle;
    private final TextView tvContent;
    private final RatingBar rtb;
    private final ImageView imgIcon;
    private final ImageView imageView;
    private final EditText editFeedback;
    private final Context context;
    private final String KEY_CHECK_OPEN_APP = "KEY CHECK OPEN APP";
    private final Button btnRate;
    private final Button btnLater;
    private OnPress onPress;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;
    private Button Send;
    private Button Cancel;

    public RatingDialog(Context context2) {
        super(context2, R.style.CustomAlertDialog);
        this.context = context2;
        setContentView(R.layout.dialog_rating_app);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(attributes);
        getWindow().setSoftInputMode(16);
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        rtb = findViewById(R.id.rtb);
        imgIcon = findViewById(R.id.imgIcon);
        imageView = findViewById(R.id.imageView);
        editFeedback = findViewById(R.id.editFeedback);
        btnRate = findViewById(R.id.btnRate);
        btnLater = findViewById(R.id.btnLater);
        onclick();
        changeRating();

    }

    public void init(Context context, OnPress onPress) {
        rtb.setRating(5f);
        this.onPress = onPress;
    }

    public void changeRating() {
        rtb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String getRating = String.valueOf(rtb.getRating());
                switch (getRating) {
                    case "1.0":
                        editFeedback.setVisibility(View.GONE);
                        btnRate.setText(context.getResources().getString(R.string.Rate_Us));
                        imgIcon.setImageResource(R.drawable.rating_1);
                        //tvTitle.setText(context.getResources().getString(R.string.Are_you_enjoy_your_app));
                        //tvContent.setText(context.getResources().getString(R.string.Please_give_us_5_stars));
                        break;
                    case "2.0":
                        editFeedback.setVisibility(View.GONE);
                        btnRate.setText(context.getResources().getString(R.string.Rate_Us));
                        imgIcon.setImageResource(R.drawable.rating_2);
                        //tvTitle.setText(context.getResources().getString(R.string.Are_you_enjoy_your_app));
                        //tvContent.setText(context.getResources().getString(R.string.Please_give_us_5_stars));
                        break;
                    case "3.0":
                        editFeedback.setVisibility(View.GONE);
                        btnRate.setText(context.getResources().getString(R.string.Rate_Us));
                        imgIcon.setImageResource(R.drawable.rating_3);
                        //tvTitle.setText(context.getResources().getString(R.string.Are_you_enjoy_your_app));
                        //tvContent.setText(context.getResources().getString(R.string.Please_give_us_5_stars));
                        break;
                    case "4.0":
                        editFeedback.setVisibility(View.GONE);
                        btnRate.setText(context.getResources().getString(R.string.Rate_Us));
                        imgIcon.setImageResource(R.drawable.rating_4);
                        //tvTitle.setText(context.getResources().getString(R.string.Are_you_enjoy_your_app));
                        //tvContent.setText(context.getResources().getString(R.string.Please_give_us_5_stars));
                        break;
                    case "5.0":
                        editFeedback.setVisibility(View.GONE);
                        btnRate.setText(context.getResources().getString(R.string.Rate_Us));
                        imgIcon.setImageResource(R.drawable.rating_4);
                        //tvTitle.setText(context.getResources().getString(R.string.Are_you_enjoy_your_app));
                        //tvContent.setText(context.getResources().getString(R.string.Please_give_us_5_stars));
                        break;
                    default:
                        btnRate.setText(context.getResources().getString(R.string.Rate_Us));
                        editFeedback.setVisibility(View.GONE);
                        imgIcon.setImageResource(R.drawable.rating_0);
                        //tvTitle.setText(context.getResources().getString(R.string.Are_you_enjoy_your_app));
                        //tvContent.setText(context.getResources().getString(R.string.Please_give_us_5_stars));
                        break;
                }
            }
        });


    }

    public String getNewName() {
        return this.editFeedback.getText().toString();
    }

    public String getRating() {
        return String.valueOf(this.rtb.getRating());
    }

    public void onclick() {
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rtb.getRating() == 0) {
                    Toast.makeText(context, context.getResources().getString(R.string.Please_feedback), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rtb.getRating() <= 3.0) {
                    imageView.setVisibility(View.GONE);
                    imgIcon.setVisibility(View.GONE);
                    onPress.send(rtb.getRating());
                } else {
                    //Edit
                    //imageView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    imgIcon.setVisibility(View.VISIBLE);
                    onPress.rating(rtb.getRating());
                }
            }
        });
        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPress.later();
            }
        });

    }

    public interface OnPress {
        void send(float rate);

        void rating(float rate);

        void cancel();

        void later();
    }

}
