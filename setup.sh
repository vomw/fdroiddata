. /etc/profile
export PATH="$fdroidserver:$PATH" PYTHONPATH="$fdroidserver"
export JAVA_HOME=$(java -XshowSettings:properties -version 2>&1 > /dev/null | grep 'java.home' | awk -F'=' '{print $2}' | tr -d ' ')

# downalod Android NDK
sdkmanager 'ndk;27.1.12297006'

# download code for scrlibs 
python3 /home/vagrant/fdroidserver/examples/fdroid_fetchsrclibs.py dev.djara.wafrn_rn

fdroid build --verbose --on-server dev.djara.wafrn_rn
