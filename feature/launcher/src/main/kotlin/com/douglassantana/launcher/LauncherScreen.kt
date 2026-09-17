package com.douglassantana.launcher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.douglassantana.designsystem.theme.AndroidSduiTheme
import com.douglassantana.domain.model.ScreenSource

/**
 * PT: Tela de seleção da fonte de dados da tela SDUI. Puramente apresentacional — sem
 * ViewModel, sem estado próprio — apenas emite [onSourceSelected] com a escolha do
 * usuário; quem chama decide o que fazer com ela (ver [com.douglassantana.android_sdui.app.MainActivity]).
 *
 * ---
 *
 * Screen for choosing the SDUI screen's data source. Purely presentational — no
 * ViewModel, no state of its own — it only emits [onSourceSelected] with the user's
 * choice; the caller decides what to do with it (see [com.douglassantana.android_sdui.app.MainActivity]).
 */
@Composable
fun LauncherScreen(
    modifier: Modifier = Modifier,
    onSourceSelected: (ScreenSource) -> Unit,
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = stringResource(R.string.launcher_title),
                modifier = Modifier.fillMaxWidth()
            )

            AndroidSduiButton(
                title = stringResource(R.string.launcher_source_backend),
                onClick = { onSourceSelected(ScreenSource.Backend) }
            )

            AndroidSduiButton(
                title = stringResource(R.string.launcher_source_remote_config),
                onClick = { onSourceSelected(ScreenSource.RemoteConfig) }
            )
        }
    }
}

@Composable
private fun AndroidSduiButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = Button(
    modifier = modifier.fillMaxWidth(),
    onClick = onClick
) {
    Text(text = title)
}

@PreviewLightDark
@Composable
private fun LauncherScreenPreview() {
    AndroidSduiTheme {
        LauncherScreen(
            onSourceSelected = {}
        )
    }
}
