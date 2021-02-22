package io.oasisbloc.wallet.base;

import androidx.annotation.NonNull;

public interface DiffItemCallback  {

    boolean areItemsTheSame(@NonNull Object object);

    boolean areContentsTheSame(@NonNull Object object);
}
