#!/usr/bin/env bash
# Valida, antes do Gradle, a credencial usada para baixar o design system do GitHub Packages.
# Lê GITHUB_TOKEN (do secret GPR_KEY) e GITHUB_ACTOR; nunca imprime o token.
set -u

fail() {
  echo "::error title=Credencial do GitHub Packages::$1"
  exit 1
}

if [ -z "${GITHUB_TOKEN:-}" ]; then
  fail "O secret GPR_KEY está vazio ou não existe. Crie-o em Settings > Secrets and variables > Actions > Repository secrets, com o nome exato GPR_KEY."
fi

user_code=$(curl -s -o /dev/null -w '%{http_code}' -H "Authorization: Bearer ${GITHUB_TOKEN}" https://api.github.com/user)
if [ "$user_code" != "200" ]; then
  fail "O GitHub recusou o token do GPR_KEY (HTTP ${user_code}). Gere um token classic com read:packages e atualize o secret."
fi

version=$(grep -E '^designsystem = "' gradle/libs.versions.toml | head -1 | cut -d'"' -f2)
pom="https://maven.pkg.github.com/diegoferreiracaetano/dlearn/com/diegoferreiracaetano/dlearn/designsystem/${version}/designsystem-${version}.pom"
pkg_code=$(curl -s -o /dev/null -w '%{http_code}' -u "${GITHUB_ACTOR}:${GITHUB_TOKEN}" "$pom")
case "$pkg_code" in
  200 | 302) echo "Credencial ok: designsystem ${version} acessível (HTTP ${pkg_code})." ;;
  401) fail "O token é válido, mas não autoriza ler pacotes (HTTP 401). Ele precisa do escopo read:packages." ;;
  404) fail "O designsystem ${version} não foi encontrado (HTTP 404). Confira a versão e o acesso ao pacote." ;;
  *) fail "Resposta inesperada ao ler o designsystem ${version} (HTTP ${pkg_code})." ;;
esac
