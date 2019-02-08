package com.nanowheel.nanoux.nanowheel.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.google.android.material.button.MaterialButton;
import com.nanowheel.nanoux.nanowheel.model.OWDevice;

public abstract class FragmentDiagnoticsBinding extends ViewDataBinding {
  @NonNull
  public final TextView distanceRecord;

  @NonNull
  public final TextView distanceTotal;

  @NonNull
  public final ImageView imageView6;

  @NonNull
  public final ImageView pitch;

  @NonNull
  public final TextView pitchText;

  @NonNull
  public final MaterialButton refreshButton;

  @NonNull
  public final ImageView roll;

  @NonNull
  public final TextView rollText;

  @NonNull
  public final TextView speedRecord;

  @Bindable
  protected OWDevice mOwdevice;

  protected FragmentDiagnoticsBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, TextView distanceRecord, TextView distanceTotal, ImageView imageView6,
      ImageView pitch, TextView pitchText, MaterialButton refreshButton, ImageView roll,
      TextView rollText, TextView speedRecord) {
    super(_bindingComponent, _root, _localFieldCount);
    this.distanceRecord = distanceRecord;
    this.distanceTotal = distanceTotal;
    this.imageView6 = imageView6;
    this.pitch = pitch;
    this.pitchText = pitchText;
    this.refreshButton = refreshButton;
    this.roll = roll;
    this.rollText = rollText;
    this.speedRecord = speedRecord;
  }

  public abstract void setOwdevice(@Nullable OWDevice owdevice);

  @Nullable
  public OWDevice getOwdevice() {
    return mOwdevice;
  }

  @NonNull
  public static FragmentDiagnoticsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentDiagnoticsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentDiagnoticsBinding>inflate(inflater, com.nanowheel.nanoux.nanowheel.R.layout.fragment_diagnotics, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentDiagnoticsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentDiagnoticsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentDiagnoticsBinding>inflate(inflater, com.nanowheel.nanoux.nanowheel.R.layout.fragment_diagnotics, null, false, component);
  }

  public static FragmentDiagnoticsBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static FragmentDiagnoticsBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (FragmentDiagnoticsBinding)bind(component, view, com.nanowheel.nanoux.nanowheel.R.layout.fragment_diagnotics);
  }
}
