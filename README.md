# Disclogs

## Sobre
Aplicativo Android inspirado em plataformas como Letterboxd, voltado para descoberta, avaliação e organização de álbuns musicais. Permite que usuários explorem discografias, registrem opiniões e interajam com outros através de uma camada social.

## Funcionalidades
- Busca de álbuns e artistas via API do Spotify
- Visualização de informações detalhadas (capa, ano, faixas)
- Avaliação de álbuns com notas e críticas
- Sistema de favoritos e listas personalizadas
- Feed social com atividades de usuários
- Interação com reviews (curtidas e comentários)
- Sistema de seguidores e acompanhamento de amigos
- Autenticação com e-mail/senha, Google e Spotify

## Tecnologias

### Android
- Kotlin
- Jetpack Compose (Material Design 3)
- Arquitetura MVVM
- Navigation Compose
- Dagger Hilt (injeção de dependência)
- Coroutines & Flow
- Coil (imagens)

### Backend & Integrações
- Supabase (Auth + PostgreSQL)
- Spotify Web API (Retrofit / OkHttp)

## Arquitetura
O projeto segue o padrão MVVM, com separação clara entre camadas de apresentação, domínio e dados. O gerenciamento de estado é feito com StateFlow, garantindo reatividade e previsibilidade.

## Status
Projeto em desenvolvimento. As principais funcionalidades já estão implementadas, com foco atual em refinamento e melhorias.

## Screenshots
<!-- adicionar aqui -->
