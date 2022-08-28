package com.bitjam.calendartracker.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bitjam.calendartracker.R;
import com.bitjam.calendartracker.SignInActivity;
import com.bitjam.calendartracker.databinding.ItemOnboardingBinding;
import com.bitjam.calendartracker.utility.Constants;

public class OnboardingFragment extends Fragment {

    private static final String TAG = "OnboardingFragment";
    
    private static final int ONBOARDING_SCREEN_1 = 0;
    private static final int ONBOARDING_SCREEN_2 = 1;
    private static final int ONBOARDING_SCREEN_3 = 2;
    private static final int ONBOARDING_SCREEN_4 = 3;

    private int contentLayoutId;
    private ItemOnboardingBinding binding;

    public OnboardingFragment() {
    }

    public OnboardingFragment(int contentLayoutId) {
        super(contentLayoutId);
        this.contentLayoutId = contentLayoutId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ItemOnboardingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return (ViewGroup) root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.onboardProgressbar.setMax(Constants.MAX_PAGES - 1);
        binding.onboardProgressbar.setProgress(contentLayoutId);

        binding.onboardSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), SignInActivity.class);
                intent.putExtra(Constants.INITIAL_SIGN_IN, true);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        switch (contentLayoutId) {
            case ONBOARDING_SCREEN_1:
                binding.onboardingDescriptionTextView.setText(R.string.onboarding_event_demo);
                binding.onboardingLottieView.setAnimation(R.raw.lottie_event_demo);
                binding.onboardSignInButton.setVisibility(View.INVISIBLE);
                break;
            case ONBOARDING_SCREEN_2:
                binding.onboardingDescriptionTextView.setText(R.string.onboarding_record_demo);
                binding.onboardingLottieView.setAnimation(R.raw.lottie_record_demo);
                binding.onboardSignInButton.setVisibility(View.INVISIBLE);
                break;
            case ONBOARDING_SCREEN_3:
                binding.onboardingDescriptionTextView.setText(R.string.onboarding_setting_demo);
                binding.onboardingLottieView.setAnimation(R.raw.lottie_setting_demo);
                binding.onboardSignInButton.setVisibility(View.INVISIBLE);
                break;
            case ONBOARDING_SCREEN_4:
                binding.onboardingDescriptionTextView.setText(R.string.onboarding_sign_in);
                binding.onboardingLottieView.setAnimation(R.raw.anim_calendar_check);
                binding.onboardSignInButton.setVisibility(View.VISIBLE);
                break;

        }
    }
}
