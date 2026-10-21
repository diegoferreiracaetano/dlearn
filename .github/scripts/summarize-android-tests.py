#!/usr/bin/env python3
"""Resume as falhas dos testes instrumentais em uma anotação do GitHub Actions."""
import glob
import sys
import xml.etree.ElementTree as ET

PATTERNS = [
    "composeApp/build/outputs/androidTest-results/**/*.xml",
    "composeApp/build/test-results/**/*.xml",
]
LIMIT = 3500

files = sorted({f for p in PATTERNS for f in glob.glob(p, recursive=True)})
failures = []
total = 0
for path in files:
    try:
        root = ET.parse(path).getroot()
    except ET.ParseError:
        continue
    for case in root.iter("testcase"):
        total += 1
        problem = case.find("failure")
        if problem is None:
            problem = case.find("error")
        if problem is not None:
            message = (problem.get("message") or problem.text or "").strip().splitlines()
            failures.append(f"{case.get('classname', '?').split('.')[-1]}.{case.get('name', '?')}: {message[0][:200] if message else ''}")

if not files:
    summary = "Nenhum relatório de teste encontrado em composeApp/build; o Gradle pode ter falhado antes de rodar os testes."
elif not failures:
    summary = f"{total} testes lidos e nenhum com falha nos relatórios; confira o log do passo."
else:
    summary = f"{len(failures)} de {total} testes falharam:\n" + "\n".join(f"- {f}" for f in failures)

encoded = summary.replace("%", "%25").replace("\r", "").replace("\n", "%0A")[:LIMIT]
print(f"::error title=Falhas nos testes instrumentados::{encoded}")
sys.exit(0)
