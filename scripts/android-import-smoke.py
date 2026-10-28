#!/usr/bin/env python3
"""Regression: external import refreshes an already-open singleTask MainActivity.

Run only on a dedicated emulator with a debug APK installed. Adds a synthetic,
non-production profile to the default group; does not connect or erase data.
Usage: python3 scripts/android-import-smoke.py [cc.bmbrvpn.android]
"""
import re
import subprocess
import sys
import time
import urllib.parse
import xml.etree.ElementTree as ET

PACKAGE = sys.argv[1] if len(sys.argv) > 1 else 'cc.bmbrvpn.android'

def adb(*args):
    return subprocess.check_output(['adb', *args], text=True, timeout=25)

def hierarchy():
    adb('shell', 'uiautomator', 'dump', '/sdcard/bomber-smoke.xml')
    return ET.fromstring(adb('shell', 'cat', '/sdcard/bomber-smoke.xml'))

def wait_label(*labels):
    for _ in range(8):
        root = hierarchy()
        for node in root.iter('node'):
            if node.get('text') in labels:
                return node
        time.sleep(1)
    raise AssertionError(f'Missing label: {labels}')

def tap_label(*labels):
    node = wait_label(*labels)
    x1, y1, x2, y2 = map(int, re.findall(r'\d+', node.get('bounds')))
    adb('shell', 'input', 'tap', str((x1+x2)//2), str((y1+y2)//2))

adb('shell', 'am', 'start', '-n', PACKAGE+'/com.v2ray.ang.ui.main.MainActivity')
tap_label('Servers', 'Серверы')
# With subscriptions present the default group may not be the visible page.
root = hierarchy()
if any(n.get('text') == 'Default' for n in root.iter('node')):
    tap_label('Default')
original_pid = adb('shell', 'pidof', PACKAGE).strip()
name = 'Import smoke '+str(time.time_ns())
profile = ('vless://11111111-1111-4111-8111-111111111111@10.0.2.2:18080'
           '?encryption=none&security=none&type=tcp#'+urllib.parse.quote(name))
link = 'bombervpn://install-config?url='+urllib.parse.quote(profile, safe='')
adb('shell', 'am', 'start', '-a', 'android.intent.action.VIEW', '-d', link, PACKAGE)
wait_label(name)
assert adb('shell', 'pidof', PACKAGE).strip() == original_pid, 'App process restarted'
print('PASS: external import visible without restarting MainActivity process')
